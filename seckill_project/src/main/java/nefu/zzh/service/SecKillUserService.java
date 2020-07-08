package nefu.zzh.service;

import nefu.zzh.commons.Result.CodeMessage;
import nefu.zzh.commons.Util.MD5Util;
import nefu.zzh.commons.exception.GlobalException;
import nefu.zzh.dao.SecKillDao;
import nefu.zzh.vo.LoginVo;
import nefu.zzh.vo.SecKillUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecKillUserService {

    @Autowired
    SecKillDao secKillDao;

    public SecKillUser getById(long id){
        return secKillDao.getById(id);
    }

    public boolean login(LoginVo loginVo) {
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
        String calcPass = MD5Util.formPassToDBPass(fromPassword, saltDB);
        if(!calcPass.equals(dbPassword)){
            throw new GlobalException(CodeMessage.PASSWORD_ERROR);
        }
        return true;
    }
}
