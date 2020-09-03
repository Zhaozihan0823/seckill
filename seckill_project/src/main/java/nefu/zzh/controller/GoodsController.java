package nefu.zzh.controller;

import nefu.zzh.commons.Redis.GoodsKey;
import nefu.zzh.commons.Redis.RedisUtil;
import nefu.zzh.commons.Result.Result;
import nefu.zzh.service.GoodsService;
import nefu.zzh.service.SecKillUserService;
import nefu.zzh.vo.GoodsDetailVo;
import nefu.zzh.vo.GoodsVo;
import nefu.zzh.vo.SecKillUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    /**
     * 查询商品列表
     * @param model
     * @param user
     * @return
     */
    @RequestMapping(value = "/toList", produces = "text/html")
    @ResponseBody
    public String list(Model model, SecKillUser user, HttpServletResponse response,
                        HttpServletRequest request){
        model.addAttribute("user", user);

        //取缓存数据
        String html = redisUtil.get(GoodsKey.getGoodsList,"",String.class);
        if (!StringUtils.isEmpty(html)){
            return html;
        }

        //查询商品列表
        List<GoodsVo> goodsList = goodsService.goodsVoList();
        model.addAttribute("goodsList", goodsList);
//        return "goods_list";

        WebContext context = new WebContext(request, response, request.getServletContext(),
                request.getLocale(), model.asMap());
        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", context);
        if (!StringUtils.isEmpty(html)){
            redisUtil.set(GoodsKey.getGoodsList,"", html);
        }
        return html;
    }

    /**
     * 查询商品详情
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/toDetail/{goodsId}", produces = "text/html")
    @ResponseBody
    public String detail(Model model, SecKillUser user, HttpServletResponse response,
                         HttpServletRequest request, @PathVariable("goodsId")long goodsId){
        model.addAttribute("user", user);

        //取缓存数据
        String html = redisUtil.get(GoodsKey.getGoodsDetail,""+goodsId,String.class);
        if (!StringUtils.isEmpty(html)){
            return html;
        }

        //查询商品详情 手动渲染
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

//        return "goods_detail";

        WebContext context = new WebContext(request, response, request.getServletContext(),
                request.getLocale(), model.asMap());
        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", context);
        if (!StringUtils.isEmpty(html)){
            redisUtil.set(GoodsKey.getGoodsDetail,""+goodsId, html);
        }
        return html;
    }

    @RequestMapping(value = "/toDetailTow/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detailTow(Model model, SecKillUser user, HttpServletResponse response,
                                           HttpServletRequest request, @PathVariable("goodsId")long goodsId){

        //查询商品详情
        GoodsVo goods = goodsService.getGoodVoByGoodsId(goodsId);
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
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        goodsDetailVo.setGoods(goods);
        goodsDetailVo.setUser(user);
        goodsDetailVo.setRemainSeconds(remainSeconds);
        goodsDetailVo.setSeckillStatus(seckillStatus);
        return Result.success(goodsDetailVo);
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
