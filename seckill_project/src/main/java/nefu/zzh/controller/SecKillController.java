package nefu.zzh.controller;

import nefu.zzh.commons.Result.CodeMessage;
import nefu.zzh.service.GoodsService;
import nefu.zzh.service.OrderService;
import nefu.zzh.service.SeckillService;
import nefu.zzh.vo.GoodsVo;
import nefu.zzh.vo.OrderInfo;
import nefu.zzh.vo.SecKillOrder;
import nefu.zzh.vo.SecKillUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/seckill")
public class SecKillController {
    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @RequestMapping("/doSeckill")
    public String doSeckill(Model model, SecKillUser user,
                            @RequestParam("goodsId")long goodsId){
        model.addAttribute("user", user);
        if (user == null){
            return "login";
        }
        //判断库存
        GoodsVo goods = goodsService.getGoodVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0){
            model.addAttribute("errmsg", CodeMessage.seckill_over.getMessage());
            return "seckillFail";
        }
        //判断是否秒杀过该商品
        SecKillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null){
            model.addAttribute("errmsg", CodeMessage.seckill_repeat.getMessage());
            return "seckillFail";
        }
        //减库存->下订单->写入秒杀订单  （事务控制）
        OrderInfo orderInfo = seckillService.seckill(user, goods);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goods);

        return "order_detail";
    }
}
