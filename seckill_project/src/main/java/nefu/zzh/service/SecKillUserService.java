package nefu.zzh.service;

import nefu.zzh.commons.Redis.RedisUtil;
import nefu.zzh.commons.Redis.SecKillUserKey;
import nefu.zzh.commons.Result.CodeMessage;
import nefu.zzh.commons.Util.MD5Util;
import nefu.zzh.commons.Util.UUIDUtil;
import nefu.zzh.commons.exception.GlobalException;
import nefu.zzh.dao.SecKillDao;
import nefu.zzh.vo.LoginVo;
import nefu.zzh.vo.SecKillUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class SecKillUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    SecKillDao secKillDao;
    @Autowired
    RedisUtil redisUtil;

    public SecKillUser getById(long id){
        return secKillDao.getById(id);
    }

    public boolean login(HttpServletResponse httpServletResponse, LoginVo loginVo) {
        if (loginVo == null){
            throw new GlobalException(CodeMessage.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String fromPassword = loginVo.getPassword();
        //判断该用户是否存在
        SecKillUser user = getById(Long.parseLong(mobile));
        if (user == null){
            throw new GlobalException(CodeMessage.MOBILE_NOT_EXIT);
        }
        //验证密码是否正确
        String dbPassword = user.getPassword();
        String saltDB = user.getSalt();
        String DBPass = MD5Util.formPassToDBPass(fromPassword, saltDB);
        if(!DBPass.equals(dbPassword)){
            throw new GlobalException(CodeMessage.PASSWORD_ERROR);
        }
        addCookie(httpServletResponse, user);

        return true;
    }

    private void addCookie(HttpServletResponse response, SecKillUser user){
        //生成cookie
        String token = UUIDUtil.uuid();
        redisUtil.set(SecKillUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(SecKillUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public SecKillUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)){
            return null;
        }
        SecKillUser user = redisUtil.get(SecKillUserKey.token, token, SecKillUser.class);
        //延长有效期
        if (user != null){
            addCookie(response, user);
        }

        return user;
    }
}
