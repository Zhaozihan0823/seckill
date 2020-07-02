package nefu.zzh.commons.Redis;

public interface KeyPrefix {

    public int expireSeconds();

    public String getPrefix();
}
