package nefu.zzh.controller;

import nefu.zzh.commons.Redis.RedisUtil;
import nefu.zzh.commons.Result.CodeMessage;
import nefu.zzh.commons.Result.Result;
import nefu.zzh.service.GoodsService;
import nefu.zzh.service.OrderService;
import nefu.zzh.service.SecKillUserService;
import nefu.zzh.vo.GoodsVo;
import nefu.zzh.vo.OrderDetailVo;
import nefu.zzh.vo.OrderInfo;
import nefu.zzh.vo.SecKillUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    SecKillUserService secKillUserService;

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisUtil redisUtil;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(Model model, SecKillUser user,
           @RequestParam("orderId")long orderId){
        if (user == null){
            return Result.error(CodeMessage.SESSION_ERROR);
        }
        OrderInfo order = orderService.getOrderById(orderId);
        if (order == null){
            return Result.error(CodeMessage.ORDER_NOT_EXIST);
        }
        long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getGoodVoByGoodsId(goodsId);
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setOrder(order);
        orderDetailVo.setGoods(goods);
        return Result.success(orderDetailVo);
    }
}
