package com.kmo.kome.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 表示系统中通用的结果状态码枚举类。
 * 每个枚举值包含三个部分：
 * 1. 状态码：用于标识操作结果的数字编码。
 * 2. 描述信息：对状态码的文字说明。
 * 3. HTTP 状态：对应的标准 HTTP 状态。
 * <p>
 * 此枚举常用于封装接口返回结果时表示不同的业务状态。
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    /**
     * 表示操作成功的常量状态码。
     * 状态码为 200，描述信息为 "Success"，对应的 HTTP 状态为 HttpStatus.OK。
     * 该枚举常量通常用于表示接口调用成功的结果。
     */
    SUCCESS(200, "Success", HttpStatus.OK),
    /**
     * 表示客户端请求无效的状态码枚举值。
     * 状态码为 400，描述信息为 "Bad Request"，对应的 HTTP 状态为 HttpStatus.BAD_REQUEST。
     * 该枚举常量通常用于处理参数错误或请求格式不合法的场景。
     */
    BAD_REQUEST(400, "Bad Request", HttpStatus.BAD_REQUEST),
    /**
     * 表示客户端未认证的状态码枚举值。
     * 状态码为 401，描述信息为 "Unauthorized"，对应的 HTTP 状态为 HttpStatus.UNAUTHORIZED。
     * 该枚举常量通常用于用户未登录或未提供有效认证信息的场景。
     */
    UNAUTHORIZED(401, "Unauthorized", HttpStatus.UNAUTHORIZED),
    /**
     * 表示客户端无权限访问资源的状态码枚举值。
     * 状态码为 403，描述信息为 "Forbidden"，对应的 HTTP 状态为 HttpStatus.FORBIDDEN。
     * 该枚举常量通常用于处理用户尝试访问受限资源或权限不足的场景。
     */
    FORBIDDEN(403, "Forbidden", HttpStatus.FORBIDDEN),
    /**
     * 表示客户端请求资源不可用或不存在的状态码枚举值。
     * 状态码为 404，描述信息为 "Not Found"，对应的 HTTP 状态为 HttpStatus.NOT_FOUND。
     * 该枚举常量通常用于处理请求资源不存在或路径无效的场景。
     */
    NOT_FOUND(404, "Not Found", HttpStatus.NOT_FOUND),
    /**
     * 表示客户端使用了不被支持的 HTTP 方法的状态码枚举值。
     * 状态码为 405，描述信息为 "Method Not Allowed"，对应的 HTTP 状态为 HttpStatus.METHOD_NOT_ALLOWED。
     * 该枚举常量通常用于处理客户端尝试使用未被服务器允许的方法（如 PUT、DELETE 等）的场景。
     */
    METHOD_NOT_ALLOWED(405, "Method Not Allowed", HttpStatus.METHOD_NOT_ALLOWED),
    /**
     * 表示服务器内部错误的状态码枚举值。
     * 状态码为 500，描述信息为 "Internal Server Error"，对应的 HTTP 状态为 HttpStatus.INTERNAL_SERVER_ERROR。
     * 该枚举常量通常用于处理不可预见的服务器端异常或错误的场景。
     */
    INTERNAL_SERVER_ERROR(500, "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);


    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
