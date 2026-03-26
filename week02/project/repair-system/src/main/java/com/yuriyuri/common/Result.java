package com.yuriyuri.common;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code; //状态码
    private String message; //提示信息
    private T data; //具体数据

    public static <T> Result<T> success(){
        Result<T> r = new Result<>();
        r.code = 200;
        r.message="操作成功";
        return r;
    }

    public static <T> Result<T> success(T data){
        Result<T> r = new Result<>();
        r.code = 200;
        r.message="操作成功";
        r.data = data;
        return r;
    }

    public static <T> Result<T> fail(String message){
        Result<T> r = new Result<>();
        r.code = 500;
        r.message=message;
        return r;
    }

    public static <T> Result<T> fail(Integer code,String message) {
        Result<T> r = new Result<>();
        r.code = code;
        r.message = message;
        return r;
    }
 }