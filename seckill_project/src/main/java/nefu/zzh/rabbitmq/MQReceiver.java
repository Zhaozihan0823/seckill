package nefu.zzh.rabbitmq;

import nefu.zzh.commons.Redis.RedisUtil;
import nefu.zzh.commons.Result.CodeMessage;
import nefu.zzh.commons.Result.Result;
import nefu.zzh.service.GoodsService;
import nefu.zzh.service.OrderService;
import nefu.zzh.service.SeckillService;
import nefu.zzh.vo.GoodsVo;
import nefu.zzh.vo.SecKillOrder;
import nefu.zzh.vo.SecKillUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MQReceiver {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;


    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @RabbitListener(queues = MQConfig.SECKILL_QUEUE)
    public void receiveSeckill(String message){
        log.info("receive message: "+message);
        SeckillMessage seckillMessage = RedisUtil.stringToBean(message, SeckillMessage.class);
        SecKillUser user = seckillMessage.getSecKillUser();
        long goodsId = seckillMessage.getGoodId();

        //判断库存
        GoodsVo goods = goodsService.getGoodVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0){
            return;
        }
        //判断是否重复秒杀
        SecKillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null){
            return;
        }
        //减库存，下订单，写入秒杀订单
        seckillService.seckill(user, goods);
    }

//    @RabbitListener(queues = MQConfig.QUEUE)
//    public void receive(String message){
//        log.info("receive message: "+message);
//    }
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
//    public void receiveTopic1(String message){
//        log.info("receive topicqueue1 message: "+message);
//    }
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
//    public void receiveTopic2(String message){
//        log.info("receive topicqueue2 message: "+message);
//    }
//    @RabbitListener(queues = MQConfig.HEADER_QUEUE)
//    public void receiveTopic2(byte[] message){
//        log.info("receive headerqueue message: "+new String(message));
//    }

}
