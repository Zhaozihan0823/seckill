package nefu.zzh.rabbitmq;


import nefu.zzh.vo.SecKillUser;

public class SeckillMessage {
    private SecKillUser secKillUser;
    private long goodId;

    public SecKillUser getSecKillUser() {
        return secKillUser;
    }

    public SeckillMessage setSecKillUser(SecKillUser secKillUser) {
        this.secKillUser = secKillUser;
        return this;
    }

    public long getGoodId() {
        return goodId;
    }

    public SeckillMessage setGoodId(long goodId) {
        this.goodId = goodId;
        return this;
    }
}
