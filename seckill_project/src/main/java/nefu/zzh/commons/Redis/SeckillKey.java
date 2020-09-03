package nefu.zzh.commons.Redis;


public class SeckillKey extends BasePrefix{

    public static SeckillKey isGoodsOver = new SeckillKey("goodsOver");

    public SeckillKey(String prefix) {
        super(prefix);
    }

}
