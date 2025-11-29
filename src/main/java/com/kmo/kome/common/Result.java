package com.kmo.kome.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 一个通用的结果对象，用于封装接口调用的响应信息。
 * 提供了状态码、提示信息、返回数据和时间戳等字段。
 * 支持构建成功和错误结果的静态方法。
 *
 * @param <T> 泛型参数，表示返回数据的类型。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    // 状态码：200成功，其他失败
    private Integer code;
    // 提示信息
    private String message;
    // 返回数据
    private T data;
    // 时间戳：当前系统时间（自动生成）
    @Builder.Default
    private Long timestamp = System.currentTimeMillis();


    /**
     * 创建一个不包含任何数据的成功结果实例。
     *
     * @param <T> 数据的类型。
     * @return 一个 {@code Result} 实例，表示操作成功，包含状态码 200 和默认的成功消息。
     */
    public static <T> Result<T> success() {
        return Result
                .<T>builder()
                .code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage())
                .build();
    }

    /**
     * 创建一个包含指定数据的成功结果实例。
     *
     * @param <T> 数据的类型。
     * @param data 要包含在成功结果中的数据。
     * @return 一个 {@code Result} 实例，表示操作成功，包含状态码 200、默认成功消息和指定数据。
     */
    public static <T> Result<T> success(T data) {
        return Result
                .<T>builder()
                .code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage())
                .data(data)
                .build();
    }


    /**
     * 创建一个包含自定义成功消息和数据的结果实例。
     *
     * @param <T> 数据的类型。
     * @param message 成功消息，描述操作结果。
     * @param data 要包含在结果中的数据。
     * @return 一个 {@code Result} 实例，表示操作成功，包含状态码 200、自定义消息和指定数据。
     */
    public static <T> Result<T> success(String message, T data) {
        return Result
                .<T>builder()
                .code(ResultCode.SUCCESS.getCode())
                .message(message)
                .data(data)
                .build();
    }

    /**
     * 创建一个通用的错误结果实例。
     *
     * @param <T> 数据的类型。
     * @return 一个 {@code Result} 实例，表示操作失败，包含状态码 500 和默认的失败消息。
     */
    public static <T> Result<T> fail() {
        return Result
                .<T>builder()
                .code(ResultCode.FAILED.getCode())
                .message(ResultCode.FAILED.getMessage())
                .build();
    }

    /**
     * 创建一个包含错误消息的结果实例。
     *
     * @param <T> 数据的类型。
     * @param message 错误消息，用于描述操作失败的原因。
     * @return 一个 {@code Result} 实例，标识操作失败，包含状态码 500 和指定的错误消息。
     */
    public static <T> Result<T> fail(String message) {
        return Result
                .<T>builder()
                .code(ResultCode.FAILED.getCode())
                .message(message)
                .build();
    }

    /**
     * 创建一个包含自定义错误码和错误消息的结果实例。
     *
     * @param <T> 数据的类型。
     * @param code 错误码，用于标识错误类型。
     * @param message 错误消息，用于描述操作失败的原因。
     * @return 一个 {@code Result} 实例，表示操作失败，包含指定的错误码和错误消息。
     */
    public static <T> Result<T> fail(Integer code, String message) {
        return Result
                .<T>builder()
                .code(code)
                .message(message)
                .build();
    }
}
