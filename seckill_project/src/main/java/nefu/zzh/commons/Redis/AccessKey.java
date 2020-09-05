package nefu.zzh.commons.Redis;


public class AccessKey extends BasePrefix{
    public AccessKey(int expireSecond, String prefix) {
        super(expireSecond, prefix);
    }

    public static AccessKey withExcepire(int expireSecond) {
        return new AccessKey(expireSecond, "access");
    }

    public static AccessKey access = new AccessKey(5, "access");

}
