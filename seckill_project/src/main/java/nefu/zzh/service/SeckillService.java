package nefu.zzh.service;

import nefu.zzh.commons.Redis.RedisUtil;
import nefu.zzh.commons.Redis.SeckillKey;
import nefu.zzh.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SeckillService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisUtil redisUtil;

    @Transactional
    public OrderInfo seckill(SecKillUser user, GoodsVo goodsVo) {
        //减库存
        boolean success = goodsService.reduceStock(goodsVo);
        if (success){
            //下订单
            return orderService.createOrder(user, goodsVo);
        }else {
            setGoodsOver(goodsVo.getId());
            return null;
        }

    }

    public long getSeckillResult(Long userId, long goodsId) {
        SecKillOrder order = orderService.getSeckillOrderByUserIdGoodsId(userId, goodsId);
        if (order != null){ //秒杀成功
            return order.getOrderId();
        }else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver){
                return -1;
            }else {
                return 0;
            }
        }
    }

    private boolean getGoodsOver(long goodsId) {
        return redisUtil.exists(SeckillKey.isGoodsOver, ""+goodsId);
    }

    private void setGoodsOver(Long goodsId) {
        redisUtil.set(SeckillKey.isGoodsOver, ""+goodsId, true);
    }

    public void reset(List<GoodsVo> goodsVoList){
        goodsService.resetStock(goodsVoList);
        orderService.deleteOrders();
    }
}
