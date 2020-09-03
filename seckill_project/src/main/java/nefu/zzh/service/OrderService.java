package nefu.zzh.service;

import nefu.zzh.commons.Redis.OrderKey;
import nefu.zzh.commons.Redis.RedisUtil;
import nefu.zzh.dao.OrderDao;
import nefu.zzh.vo.GoodsVo;
import nefu.zzh.vo.OrderInfo;
import nefu.zzh.vo.SecKillOrder;
import nefu.zzh.vo.SecKillUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    RedisUtil redisUtil;


    public SecKillOrder getSeckillOrderByUserIdGoodsId(long userId, long goodsId) {
//        return orderDao.getSeckillOrderByUserIdGoodsId(userId,goodsId);
        return redisUtil.get(OrderKey.getSeckillOrderByUidGid, ""+userId+"_"+goodsId, SecKillOrder.class);
    }

    @Transactional
    public OrderInfo createOrder(SecKillUser user, GoodsVo goodsVo) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goodsVo.getId());
        orderInfo.setGoodsName(goodsVo.getGoodsName());
        orderInfo.setGoodsPrice(goodsVo.getSeckillPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderDao.insert(orderInfo);

        SecKillOrder secKillOrder = new SecKillOrder();
        secKillOrder.setGoodsId(goodsVo.getId());
        secKillOrder.setOrderId(orderInfo.getId());
        secKillOrder.setUserId(user.getId());

        orderDao.insertSecKillOrder(secKillOrder);
        redisUtil.set(OrderKey.getSeckillOrderByUidGid, ""+user.getId()+"_"+ goodsVo.getId(), secKillOrder);

        return orderInfo;
    }

    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }

    public void deleteOrders() {
        orderDao.deleteOrders();
        orderDao.deleteSeckillOrders();
    }
}
