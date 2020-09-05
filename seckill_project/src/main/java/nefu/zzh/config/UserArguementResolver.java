package nefu.zzh.config;


import nefu.zzh.commons.access.UserContext;
import nefu.zzh.service.SecKillUserService;
import nefu.zzh.vo.SecKillUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserArguementResolver implements HandlerMethodArgumentResolver {

    @Autowired
    SecKillUserService secKillUserService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazz = methodParameter.getParameterType();
        return clazz == SecKillUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
//        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
//        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
//
//        String paramToken = request.getParameter(SecKillUserService.COOKIE_NAME_TOKEN);
//        String cookieToken = getCookieValue(request, SecKillUserService.COOKIE_NAME_TOKEN);
//        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
//            return null;
//        }
//        String token = StringUtils.isEmpty(paramToken)?cookieToken : paramToken;

        return UserContext.getUser();
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
