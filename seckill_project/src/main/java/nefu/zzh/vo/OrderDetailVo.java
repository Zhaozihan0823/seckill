package nefu.zzh.vo;

public class OrderDetailVo {
    private GoodsVo goods;
    private OrderInfo order;

    public GoodsVo getGoods() {
        return goods;
    }

    public OrderDetailVo setGoods(GoodsVo goods) {
        this.goods = goods;
        return this;
    }

    public OrderInfo getOrder() {
        return order;
    }

    public OrderDetailVo setOrder(OrderInfo order) {
        this.order = order;
        return this;
    }
}
