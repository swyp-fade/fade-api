package com.fade.global.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Response<T> {

    private int statusCode;
    private String message;
    private T result;

    public static <T> Response<T> success() {
        return new Response<T>(200, "요청에 성공했습니다.", null);
    }

    public static <T> Response<T> success(int statusCode, String message, T result) {
        return new Response<>(statusCode, message, result);
    }

    public static <T> Response<T> error(int statusCode, String message, T result) {
        return new Response<>(statusCode, message, result);
    }

    @Builder
    private Response(int statusCode, String message, T result) {
        this.statusCode = statusCode;
        this.message = message;
        this.result = result;
    }
}
