package nefu.zzh.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import nefu.zzh.commons.Redis.GoodsKey;
import nefu.zzh.commons.Redis.OrderKey;
import nefu.zzh.commons.Redis.RedisUtil;
import nefu.zzh.commons.Redis.SeckillKey;
import nefu.zzh.commons.Result.CodeMessage;
import nefu.zzh.commons.Result.Result;
import nefu.zzh.rabbitmq.MQSender;
import nefu.zzh.rabbitmq.SeckillMessage;
import nefu.zzh.service.GoodsService;
import nefu.zzh.service.OrderService;
import nefu.zzh.service.SeckillService;
import nefu.zzh.vo.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/seckill")
public class SecKillController implements InitializingBean {
    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    MQSender sender;

    private Map<Long, Boolean> map = new HashMap<Long, Boolean>();

    /**
     * GET 与 POST 区别
     * GET幂等，从服务端获取数据，不论请求多少次，不会对服务端影响
     * POST非幂等，对服务端提交数据
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/doSeckill", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> doSeckill(Model model, SecKillUser user,
                                       @RequestParam("goodsId")long goodsId){
        model.addAttribute("user", user);
        if (user == null){
            return Result.error(CodeMessage.SESSION_ERROR);
        }
        //利用内存标记减少redis访问
        boolean over = map.get(goodsId);
        if (over){
            return Result.error(CodeMessage.SECKILL_OVER);
        }
        //预减库存
        long stock = redisUtil.decr(GoodsKey.getSeckillGoodsStock, ""+goodsId);
        if (stock < 0){
            map.put(goodsId, true);
            return Result.error(CodeMessage.SECKILL_OVER);
        }
        //判断是否重复秒杀
        SecKillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMessage.SECKILL_REPEAT);
        }
        //入队
        SeckillMessage seckillMessage = new SeckillMessage();
        seckillMessage.setSecKillUser(user);
        seckillMessage.setGoodId(goodsId);
//        System.out.println("sender 进入队列");
        sender.sendSeckillMessage(seckillMessage);
        return Result.success(1);   //排队中

        /*
        //判断库存
        GoodsVo goods = goodsService.getGoodVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0){
            return Result.error(CodeMessage.SECKILL_OVER);
//            model.addAttribute("errmsg", CodeMessage.SECKILL_OVER.getMessage());
//            return "seckillFail";
        }
        //判断是否秒杀过该商品
        SecKillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null){
            return Result.error(CodeMessage.SECKILL_REPEAT);
//            model.addAttribute("errmsg", CodeMessage.SECKILL_REPEAT.getMessage());
//            return "seckillFail";
        }
        //减库存->下订单->写入秒杀订单  （事务控制）
        OrderInfo orderInfo = seckillService.seckill(user, goods);
        return Result.success(orderInfo);

         */
    }

    /**
     * 系统初始化
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.goodsVoList();
        if (goodsList == null){
            return;
        }
        for (GoodsVo goods : goodsList){
            redisUtil.set(GoodsKey.getSeckillGoodsStock, ""+goods.getId(), goods.getStockCount());
            map.put(goods.getId(), false);
        }
    }

    /**
     * orderId:成功
     * -1：秒杀失败
     * 0：排队中
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> doSeckillResult(Model model, SecKillUser user,
                                     @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMessage.SESSION_ERROR);
        }
        long result = seckillService.getSeckillResult(user.getId(), goodsId);
        return Result.success(result);
    }

    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    @ResponseBody
    public Result<Boolean> reset(Model model){
        List<GoodsVo> goodsVoList = goodsService.goodsVoList();
        for (GoodsVo goods : goodsVoList){
            goods.setStockCount(10);
            redisUtil.set(GoodsKey.getSeckillGoodsStock, ""+goods.getId(), 10);
            map.put(goods.getId(), false);
        }
        redisUtil.delete(OrderKey.getSeckillOrderByUidGid);
        redisUtil.delete(SeckillKey.isGoodsOver);
        seckillService.reset(goodsVoList);
        return Result.success(true);
    }
}
