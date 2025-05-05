package dev.kkl.bookingsystem.base.common;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class AppResponse implements Serializable {
    public String msg;
    public Object data;
    public Integer statusCode;
    private long timestamp;

    public static AppResponse fail(String errorMsg) {
        return AppResponse.builder()
                .statusCode(Constant.FAILURE_CODE)
                .msg(errorMsg)
                .data("*****")
                .timestamp(System.currentTimeMillis()/1000)
                .build();
    }
    public static AppResponse success(String msg, Object data) {
        return AppResponse.builder()
                .statusCode(Constant.SUCCESS_CODE)
                .msg(msg)
                .data(data)
                .timestamp(System.currentTimeMillis()/1000)
                .build();
    }
    public static AppResponse fail(String errorMsg, Object data) {
        return AppResponse.builder()
                .statusCode(Constant.FAILURE_CODE)
                .msg(errorMsg)
                .data(data.toString())
                .timestamp(System.currentTimeMillis()/1000)
                .build();
    }
}
