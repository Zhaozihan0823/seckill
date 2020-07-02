package nefu.zzh.controller;

import nefu.zzh.commons.Redis.RedisUtil;
import nefu.zzh.commons.Redis.UserKey;
import nefu.zzh.commons.Result.Result;
import nefu.zzh.service.UserService;
import nefu.zzh.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping
public class SampleController {

    @Autowired
    UserService userService;

    @Autowired
    RedisUtil redisUtil;

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name", "jack");
        return "hello";
    }

    @RequestMapping("/db/tx")
    @ResponseBody
    public Result<Boolean> dbTx(){
        userService.Tx();
        return Result.success(Boolean.TRUE);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet(){
        User result = redisUtil.get(UserKey.getById,"key", User.class);
        return Result.success(result);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet(){
        User user = new User();
        user.setId(1);
        user.setName("tom");
        Boolean res = redisUtil.set(UserKey.getById,""+1, user);
        User result = redisUtil.get(UserKey.getById,""+1, User.class);
        System.out.println(result);
        return Result.success(Boolean.TRUE);
    }
}
