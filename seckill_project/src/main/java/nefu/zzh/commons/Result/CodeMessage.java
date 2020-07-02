package nefu.zzh.commons.Result;

public class CodeMessage {
    private Integer code;
    private String message;

    //通用异常定义
    public static CodeMessage SUCCESS = new CodeMessage(1, "success");
    public static CodeMessage SERVER_ERROR = new CodeMessage(500100, "服务端异常");
    //登录模块 5002XX

    //商品模块 5003XX

    //订单模块 5004XX

    //秒杀模块 5005XX

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
}
