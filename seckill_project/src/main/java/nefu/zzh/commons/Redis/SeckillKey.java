package nefu.zzh.commons.Redis;


public class SeckillKey extends BasePrefix{

    public static SeckillKey isGoodsOver = new SeckillKey(0, "goodsOver");
    public static SeckillKey getSeckillPath = new SeckillKey(60, "seckillPath");
    public static SeckillKey getVerifyCode = new SeckillKey(300, "verifyCode");

    public SeckillKey( int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

}
