package nefu.zzh.service;

import nefu.zzh.vo.Goods;
import nefu.zzh.vo.GoodsVo;
import nefu.zzh.vo.OrderInfo;
import nefu.zzh.vo.SecKillUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeckillService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Transactional
    public OrderInfo seckill(SecKillUser user, GoodsVo goodsVo) {
        //减库存
        goodsService.reduceStock(goodsVo);
        //下订单
        return orderService.createOrder(user, goodsVo);
    }
}
