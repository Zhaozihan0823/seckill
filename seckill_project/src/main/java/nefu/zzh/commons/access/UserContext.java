package nefu.zzh.commons.access;


import nefu.zzh.vo.SecKillUser;

public class UserContext {
    private static ThreadLocal<SecKillUser> userThreadLocal = new ThreadLocal<SecKillUser>();

    public static void setUser(SecKillUser user){
        userThreadLocal.set(user);
    }

    public static SecKillUser getUser(){
        return userThreadLocal.get();
    }

}
