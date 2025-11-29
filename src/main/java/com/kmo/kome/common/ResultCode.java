package com.kmo.kome.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 表示操作结果状态的枚举类，包含常用的状态码及其对应信息。
 * 该枚举被广泛用于定义和描述接口请求的通用状态，并与 {@code Result} 类协同使用。
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(200, "Success"),
    FAILED(500, "Internal Server Error"),
    VALIDATE_FAILED(400, "Validation Failed"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found");

    private final Integer code;
    private final String message;
}
