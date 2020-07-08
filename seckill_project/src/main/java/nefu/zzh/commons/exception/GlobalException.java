package nefu.zzh.commons.exception;

import nefu.zzh.commons.Result.CodeMessage;

public class GlobalException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private CodeMessage codeMessage;

    public GlobalException(CodeMessage message){
        super(message.toString());
        this.codeMessage = message;
    }

    public CodeMessage getcodeMessage() {
        return codeMessage;
    }
}
