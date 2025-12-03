package com.kmo.kome.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 表示接口返回结果的通用类，封装了状态码、提示信息、数据信息以及时间戳。
 * 支持通过静态方法快捷创建成功或失败的结果实例。
 *
 * @param <T> 返回数据的类型。
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
     * 创建一个包含指定错误代码的结果实例。
     *
     * @param <T> 数据的类型。
     * @param resultCode 错误代码对象，包含错误状态码和默认错误消息。
     * @return 一个 {@code Result} 实例，表示操作失败，包含指定的错误状态码和默认的错误消息。
     */
    public static <T> Result<T> fail(ResultCode resultCode) {
        return Result
                .<T>builder()
                .code(resultCode.getCode())
                .message(resultCode.getMessage())
                .build();
    }

    /**
     * 创建一个包含指定错误代码和错误消息的结果实例。
     *
     * @param <T> 数据的类型。
     * @param resultCode 错误代码对象，包含错误状态码和默认错误消息。
     * @param message 自定义的错误消息，用于覆盖默认的错误描述。
     * @return 一个 {@code Result} 实例，表示操作失败，包含指定的状态码和自定义的错误消息。
     */
    public static <T> Result<T> fail(ResultCode resultCode, String message) {
        return Result
                .<T>builder()
                .code(resultCode.getCode())
                .message(message)
                .build();
    }
}
