package com.energy.function.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    //成功响应
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    //失败响应
    public static Result<?> fail(String message) {
        return new Result<>(500, message, null);
    }

    //参数校验失败响应
    public static Result<?> validFail(String message) {
        return new Result<>(404, message, null);
    }
}
