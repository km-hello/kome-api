package com.kmo.kome.common.exception;

import com.kmo.kome.common.ResultCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 自定义服务异常类，用于封装业务逻辑中的异常信息。
 * 继承自 {@code RuntimeException}，支持运行时抛出。
 * 包含错误码和错误信息，用于描述具体的异常情况。
 */
@Getter
public class ServiceException extends RuntimeException{
    private final Integer code;
    private final ResultCode resultCode;

    /**
     * 使用指定的结果代码创建服务异常。
     * 该构造方法通过传入的结果代码枚举初始化异常的错误消息和错误码。
     *
     * @param resultCode 结果代码枚举，包含错误码、错误消息及对应的 HTTP 状态码。
     */
    public ServiceException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.resultCode = resultCode;
    }

    /**
     * 使用指定的结果代码和自定义错误消息创建服务异常。
     * 该构造方法允许传入自定义的错误描述，但错误码仍然从结果代码枚举中获取。
     *
     * @param resultCode 结果代码枚举，包含错误码和默认错误消息。
     * @param message 自定义的错误消息，可用于覆盖结果代码中的默认错误消息。
     */
    public ServiceException(ResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.getCode();
        this.resultCode = resultCode;
    }

    /**
     * 获取与当前服务异常相关联的 HTTP 状态码。
     * 该方法从异常的结果代码中提取对应的 HTTP 状态码，用于统一封装和响应业务操作的 HTTP 状态。
     *
     * @return HTTP 状态码，表示异常对应的标准 HTTP 状态信息。
     */
    public HttpStatus getHttpStatus(){
        return resultCode.getHttpStatus();
    }
}
