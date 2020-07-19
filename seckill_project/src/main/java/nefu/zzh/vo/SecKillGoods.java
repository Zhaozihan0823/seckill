package nefu.zzh.vo;


import java.util.Date;

public class SecKillGoods {
    private Long id;
    private Long goodsId;
    private double seckillPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getGoodsId() {
        return goodsId;
    }
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public double getSeckillPrice() {
        return seckillPrice;
    }

    public SecKillGoods setSeckillPrice(double seckillPrice) {
        this.seckillPrice = seckillPrice;
        return this;
    }

    public Integer getStockCount() {
        return stockCount;
    }
    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
