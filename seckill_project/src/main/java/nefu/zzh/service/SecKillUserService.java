package nefu.zzh.service;

import nefu.zzh.commons.Redis.RedisUtil;
import nefu.zzh.commons.Redis.SecKillUserKey;
import nefu.zzh.commons.Result.CodeMessage;
import nefu.zzh.commons.Util.MD5Util;
import nefu.zzh.commons.Util.UUIDUtil;
import nefu.zzh.commons.exception.GlobalException;
import nefu.zzh.dao.SecKillUserDao;
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
    SecKillUserDao secKillUserDao;
    @Autowired
    RedisUtil redisUtil;

    public SecKillUser getById(long id){
        //取缓存
        SecKillUser user = redisUtil.get(SecKillUserKey.getById, ""+id, SecKillUser.class);
        if (user != null){
            return user;
        }
        user = secKillUserDao.getById(id);
        if (user != null){
            redisUtil.set(SecKillUserKey.getById,""+id, user);
        }
        return user;
//        return secKillUserDao.getById(id);
    }

    public boolean updatePassword(String token, long id, String frompassword){
        //取user
        SecKillUser user = getById(id);
        if (user == null){
            throw new GlobalException(CodeMessage.MOBILE_NOT_EXIT);
        }
        //更新数据库
        SecKillUser toBeUpdate = new SecKillUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(frompassword, user.getSalt()));
        secKillUserDao.update(toBeUpdate);
        //处理所有涉及到的缓存,根据token和id查找的缓存都需要更新
        redisUtil.delete(SecKillUserKey.getById, ""+id);
        user.setPassword(toBeUpdate.getPassword());
        redisUtil.set(SecKillUserKey.token, token, user);
        return true;
    }

    public String login(HttpServletResponse httpServletResponse, LoginVo loginVo) {
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
        String token = UUIDUtil.uuid();
        addCookie(httpServletResponse, token, user);

        return token;
    }

    private void addCookie(HttpServletResponse response, String token, SecKillUser user){
        //生成cookie
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
            addCookie(response, token, user);
        }

        return user;
    }
}
