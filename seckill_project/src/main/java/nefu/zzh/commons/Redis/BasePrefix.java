package nefu.zzh.commons.Redis;


public abstract class BasePrefix implements KeyPrefix{

    private int expireSecond;
    private String prefix;

    /**
     * 过期时间默认为0，代表永不过期
     * @param prefix
     */
    public BasePrefix(String prefix) {
        this(0,prefix);
    }

    public BasePrefix(int expireSecond, String prefix) {
        this.expireSecond = expireSecond;
        this.prefix = prefix;
    }

    /**
     * 过期时间设置，默认0为永不过期
     * @return expireSecond
     */
    @Override
    public int expireSeconds() {
        return expireSecond;
    }

    /**
     * 设置区分前缀，以类名来进行区分
     * @return
     */
    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className+":"+prefix;
    }
}
