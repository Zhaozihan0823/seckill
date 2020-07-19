package nefu.zzh.controller;

import nefu.zzh.commons.Redis.RedisUtil;
import nefu.zzh.service.GoodsService;
import nefu.zzh.service.SecKillUserService;
import nefu.zzh.vo.GoodsVo;
import nefu.zzh.vo.SecKillUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    GoodsService goodsService;
    @Autowired
    SecKillUserService secKillUserService;
    @Autowired
    RedisUtil redisUtil;

    /**
     * 查询商品列表
     * @param model
     * @param user
     * @return
     */
    @RequestMapping("/toList")
    public String list(Model model, SecKillUser user){
        model.addAttribute("user", user);
        //查询商品列表
        List<GoodsVo> goodsList = goodsService.goodsVoList();
        model.addAttribute("goodsList", goodsList);
        return "goods_list";
    }

    /**
     * 查询商品详情
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping("/toDetail/{goodsId}")
    public String detail(Model model, SecKillUser user,
                         @PathVariable("goodsId")long goodsId){
        model.addAttribute("user", user);
        //查询商品详情
        GoodsVo goods = goodsService.getGoodVoByGoodsId(goodsId);
        model.addAttribute("goods",goods);
        long start = goods.getStartDate().getTime();
        long end = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int seckillStatus = 0;  //秒杀状态
        int remainSeconds = 0;  //距离秒杀开始时间
        if(now < start){//秒杀还未开始
            seckillStatus = 0;
            remainSeconds = (int)(start-now)/1000;
        }else if (now > end){//秒杀已结束
            seckillStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            seckillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("seckillStatus", seckillStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        return "goods_detail";
    }

//    @RequestMapping("/toDetail")
//    public String detail(HttpServletResponse response, Model model,
//                       @CookieValue(value = SecKillUserService.COOKIE_NAME_TOKEN, required = false)String cookieToken,
//                       @RequestParam(value = SecKillUserService.COOKIE_NAME_TOKEN, required = false)String paramToken){
//        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
//            return "login";
//        }
//        String token = StringUtils.isEmpty(paramToken)?cookieToken : paramToken;
//        SecKillUser user = secKillUserService.getByToken(response, token);
//        model.addAttribute("user", user);
//        return "goods_list";
//    }
}
