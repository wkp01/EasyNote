package com.xmaptech.entity;

import com.xmaptech.constants.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response <T> {

    private int code;
    private String msg;
    private T data; // 返回的数据

    public static <T> Response<T> success(Result result,T data) {
        return new Response<T>(result.getCode(), result.getMsg(), data);
    }

    public static <T> Response<T> error(Result result) {
        return new Response<T>(result.getCode(), result.getMsg(), null);
    }
}
