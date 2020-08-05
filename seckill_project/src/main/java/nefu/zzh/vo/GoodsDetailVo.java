package nefu.zzh.vo;

public class GoodsDetailVo {
    private int seckillStatus = 0;
    private int remainSeconds = 0;
    private GoodsVo goods;
    private SecKillUser user;

    public SecKillUser getUser() {
        return user;
    }

    public GoodsDetailVo setUser(SecKillUser user) {
        this.user = user;
        return this;
    }

    public int getSeckillStatus() {
        return seckillStatus;
    }

    public GoodsDetailVo setSeckillStatus(int seckillStatus) {
        this.seckillStatus = seckillStatus;
        return this;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public GoodsDetailVo setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
        return this;
    }

    public GoodsVo getGoods() {
        return goods;
    }

    public GoodsDetailVo setGoods(GoodsVo goods) {
        this.goods = goods;
        return this;
    }
}
