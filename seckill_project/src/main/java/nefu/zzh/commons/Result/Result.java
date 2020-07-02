package nefu.zzh.commons.Result;

public class Result<T> {

    private Integer code;
    private String message;
    private T data;

    /**
     * 成功返回调用
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data){
        return new Result<T>(data);
    }

    /**
     * 失败返回调用
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(CodeMessage codeMessage){
        return new Result<T>(codeMessage);
    }

    private Result(T data) {
        this.code = 1;
        this.message = "success";
        this.data = data;
    }

    private Result(CodeMessage codeMessage) {
        if(codeMessage == null){
            return;
        }
        this.code = codeMessage.getCode();
        this.message = codeMessage.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result() {
    }
}
