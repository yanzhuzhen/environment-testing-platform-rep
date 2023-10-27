package Exmpl.Utils;

import lombok.Data;

@Data
public class Result<T>{
    private Boolean success;
    private int code;
    private String message;
    private T data;

    private Result() {}

    public  static <T> Result<T> ok() {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(resultCode.SUCCESS);
        r.setMessage("操作成功");
        return r;
    }

    public  static <T> Result<T> ok(T data) {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(resultCode.SUCCESS);
        r.setData(data);
        r.setMessage("操作成功");
        return r;
    }

    public  static <T> Result<T> error() {
        Result<T> r = new Result<T>();
        r.setSuccess(false);
        r.setCode(resultCode.ERROR);
        r.setMessage("操作失败");
        return r;
    }

    public Result<T> success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public Result<T> code(int code){
        this.setCode(code);
        return this;
    }

    public Result<T> message(String message){
        this.setMessage(message);
        return this;
    }

    public static<T> Result<T> exist(){
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(resultCode.SUCCESS);
        r.setMessage("用户已存在");
        return r;
    }

}
