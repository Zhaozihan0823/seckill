package nefu.zzh.commons.access;

import com.alibaba.fastjson.JSON;
import nefu.zzh.commons.Redis.AccessKey;
import nefu.zzh.commons.Redis.RedisUtil;
import nefu.zzh.commons.Redis.SeckillKey;
import nefu.zzh.commons.Result.CodeMessage;
import nefu.zzh.commons.Result.Result;
import nefu.zzh.service.SecKillUserService;
import nefu.zzh.vo.SecKillUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    SecKillUserService secKillUserService;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod){
            SecKillUser user = getUser(request, response);
            UserContext.setUser(user);
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null){
                return true;
            }

            int seconds = accessLimit.seconds();
            int macCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();

            String key = request.getRequestURI();
            if (needLogin){
                if (user == null){
                    render(response, CodeMessage.SESSION_ERROR);
                    return false;
                }
                key += "-"+user.getId();
            }else {
                //do nothing
            }
            //重构-改善既有代码的设计
            AccessKey accessKey = AccessKey.withExcepire(seconds);
            Integer count = redisUtil.get(accessKey, key, Integer.class);
            if (count == null){
                redisUtil.set(accessKey, key, 1);
            }else if (count < macCount){
                redisUtil.incr(accessKey, key);
            }else {
                render(response, CodeMessage.ACCESS_LIMIT_REACHED);
                return false;
            }
        }
        return true;
    }

    private void render(HttpServletResponse response, CodeMessage codeMessage) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream outputStream = response.getOutputStream();
        JSON.toJSONString(Result.error(codeMessage));
        outputStream.write(toString().getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }

    private SecKillUser getUser(HttpServletRequest request, HttpServletResponse response){
        String paramToken = request.getParameter(SecKillUserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request, SecKillUserService.COOKIE_NAME_TOKEN);
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
            return null;
        }
        String token = StringUtils.isEmpty(paramToken)?cookieToken : paramToken;

        return secKillUserService.getByToken(response, token);
    }

    private String getCookieValue(HttpServletRequest request, String cookieNameToken) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null || cookies.length <= 0){
            return null;
        }
        for (Cookie cookie : cookies){
            if (cookie.getName().equals(cookieNameToken)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
