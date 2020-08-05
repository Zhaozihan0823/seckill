package nefu.zzh.commons.Redis;

public class GoodsKey extends BasePrefix{

    public static GoodsKey getGoodsList = new GoodsKey(60, "goodsList");
    public static GoodsKey getGoodsDetail = new GoodsKey(60, "goodsDetail");


    public GoodsKey(int expireSecond, String prefix) {
        super(expireSecond, prefix);
    }

}
