package com.kmo.kome.common.exception;

import com.kmo.kome.common.ResultCode;
import lombok.Getter;

/**
 * 自定义服务异常类，用于封装业务逻辑中的异常信息。
 * 继承自 {@code RuntimeException}，支持运行时抛出。
 * 包含错误码和错误信息，用于描述具体的异常情况。
 */
@Getter
public class ServiceException extends RuntimeException{
    private final Integer code;

    /**
     * 创建一个带有自定义错误消息的服务异常。
     * 当业务逻辑中出现需要明确的错误提示时，可以通过该构造方法抛出异常。
     *
     * @param message 异常信息，用于描述具体的错误或异常原因。
     */
    public ServiceException(String message) {
        super(message);
        this.code = ResultCode.FAILED.getCode();
    }

    /**
     * 使用指定的结果代码创建服务异常。
     * 构造方法将结果代码中的错误消息和错误码赋值到异常的相应属性中。
     *
     * @param resultCode 结果代码枚举，包含错误码和错误消息，用于描述异常原因。
     */
    public ServiceException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    /**
     * 使用自定义的错误码和错误信息创建服务异常。
     * 该构造方法适用于需要同时指定错误码和错误信息的场景。
     *
     * @param code 错误码，用于标识具体的错误类型。
     * @param message 异常信息，用于描述具体的错误或异常原因。
     */
    public ServiceException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
