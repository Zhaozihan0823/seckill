package nefu.zzh.commons.Redis;

/**
 * Classname:OrderKey
 * Package:nefu.zzh.commons.Redis
 * Description:
 *
 * @DATE:2020/7/2 18:28
 * @Author:zhaozihan0823
 */
public class OrderKey extends BasePrefix {
    public OrderKey(int expireSecond, String prefix) {
        super(expireSecond, prefix);
    }
}
