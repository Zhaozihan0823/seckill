package nefu.zzh.controller;

import nefu.zzh.commons.Redis.RedisUtil;
import nefu.zzh.service.SecKillUserService;
import nefu.zzh.vo.SecKillUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    SecKillUserService secKillUserService;
    @Autowired
    RedisUtil redisUtil;

    @RequestMapping("/toList")
    public String list(Model model, SecKillUser user){
        model.addAttribute("user", user);
        return "goods_list";
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
