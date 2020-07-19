package nefu.zzh.vo;


import java.util.Date;

public class GoodsVo extends Goods{
    private double seckillPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;

    public double getSeckillPrice() {
        return seckillPrice;
    }

    public GoodsVo setSeckillPrice(double seckillPrice) {
        this.seckillPrice = seckillPrice;
        return this;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public GoodsVo setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
        return this;
    }

    public Date getStartDate() {
        return startDate;
    }

    public GoodsVo setStartDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public Date getEndDate() {
        return endDate;
    }

    public GoodsVo setEndDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }
}
