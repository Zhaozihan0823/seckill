package nefu.zzh.commons.Redis;

public class GoodsKey extends BasePrefix{

    public static GoodsKey getGoodsList = new GoodsKey(60, "goodsList");
    public static GoodsKey getGoodsDetail = new GoodsKey(60, "goodsDetail");
    public static GoodsKey getSeckillGoodsStock = new GoodsKey(0, "goodsStock");

    public GoodsKey(int expireSecond, String prefix) {
        super(expireSecond, prefix);
    }

}
