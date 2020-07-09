package nefu.zzh.controller;


import nefu.zzh.commons.Result.Result;
import nefu.zzh.service.SecKillUserService;
import nefu.zzh.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    SecKillUserService secKillUserService;

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/doLogin")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo){
        //log.info(loginVo.toString());
        secKillUserService.login(response, loginVo);

        return Result.success(true);
    }

}
