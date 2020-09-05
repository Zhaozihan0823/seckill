package nefu.zzh.commons.Result;

public class CodeMessage {
    private Integer code;
    private String message;

    //通用异常定义
    public static CodeMessage SUCCESS = new CodeMessage(1, "success");
    public static CodeMessage SERVER_ERROR = new CodeMessage(500100, "服务端异常");
    public static CodeMessage BIND_ERROR = new CodeMessage(500101, "参数校验异常：%s");
    public static CodeMessage  REQUEST_ILLEGAL = new CodeMessage(500102, "请求非法");
    public static CodeMessage  ACCESS_LIMIT_REACHED = new CodeMessage(500103, "访问次数太频繁");

    //登录模块 5002XX
    public static CodeMessage SESSION_ERROR = new CodeMessage(500200, "session不存在或者已失效");
    public static CodeMessage PASSWORD_EMPTY = new CodeMessage(500211, "登陆密码不能为空");
    public static CodeMessage MOBILE_EMPTY = new CodeMessage(500212, "手机号不能为空");
    public static CodeMessage MOBILE_ERROR = new CodeMessage(500213, "手机号格式错误");
    public static CodeMessage MOBILE_NOT_EXIT = new CodeMessage(500214, "该用户不存在");
    public static CodeMessage PASSWORD_ERROR = new CodeMessage(500215, "用户密码错误");

    //商品模块 5003XX

    //订单模块 5004XX
    public static CodeMessage ORDER_NOT_EXIST = new CodeMessage(500400, "订单不存在");

    //秒杀模块 5005XX
    public static CodeMessage  SECKILL_OVER= new CodeMessage(500500, "商品库存不足");
    public static CodeMessage  SECKILL_REPEAT= new CodeMessage(500501, "不能重复秒杀");
    public static CodeMessage  SECKILL_FAIL= new CodeMessage(500502, "秒杀失败");


    private CodeMessage(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public CodeMessage() {
    }

    public Integer getCode() {
        return code;
    }

    public CodeMessage setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public CodeMessage setMessage(String message) {
        this.message = message;
        return this;
    }

    public CodeMessage fillArgs(Object... args){
        int code = this.code;
        String message = String.format(this.message, args);
        return new CodeMessage(code, message);
    }
}
