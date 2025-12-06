package com.kmo.kome.common.exception;

import com.kmo.kome.common.Result;
import com.kmo.kome.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;


/**
 * 全局异常处理器，用于捕获和处理系统运行过程中出现的各种异常。
 * 通过集中处理异常，可以提供统一的错误响应格式，提高系统的可维护性和用户体验。
 * 支持处理身份认证、业务逻辑、参数校验、权限不足以及未知异常等场景。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常的方法。当捕获到 {@code ServiceException} 异常时，
     * 此方法会构建统一的错误响应，并返回适当的 HTTP 状态码和错误信息。
     *
     * @param e 业务异常对象 {@code ServiceException}，包含异常的错误码、错误信息及对应的 HTTP 状态码等详细信息。
     * @return 一个 {@code ResponseEntity} 对象，其中封装了错误信息 {@code Result} 实例，并带有对应的 HTTP 状态码。
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Result<?>> handleServiceException(ServiceException e){
        log.error("业务异常: code={}, message={}", e.getCode(), e.getMessage());
        // 构建响应体
        Result<?> result = Result.fail(e.getResultCode(), e.getMessage());
        // 从异常对象中直接获取正确的 HTTP 状态码
        HttpStatus status = e.getHttpStatus();
        // 封装成 ResponseEntity 返回
        return new ResponseEntity<>(result, status);
    }

    /**
     * 处理静态资源未找到异常的方法。
     * 捕获 {@code NoResourceFoundException} 并返回统一的错误响应。
     *
     * @param e 捕获到的 {@code NoResourceFoundException} 异常对象，包含异常的详细信息。
     * @return 一个 {@code ResponseEntity} 对象，其中包含封装好的错误信息 {@code Result} 实例，状态码为 404。
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Result<?>> handleNoResourceFoundException(NoResourceFoundException e){
        log.warn("无静态资源: {}", e.getMessage());
        Result<?> result = Result.fail(ResultCode.NOT_FOUND);
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

    /**
     * 处理客户端使用不支持的 HTTP 方法时抛出的异常。
     * 捕获 {@code HttpRequestMethodNotSupportedException} 并返回统一的错误响应。
     *
     * @param e 捕获到的 {@code HttpRequestMethodNotSupportedException} 异常对象，包含不受支持的 HTTP 方法详情。
     * @return 一个 {@code ResponseEntity} 对象，其中包含封装的错误信息 {@code Result} 实例，状态码为 405。
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<Result<?>> handleHttpRequestMethodNotSupported (HttpRequestMethodNotSupportedException e){
        log.warn("不支持的请求方法：{}", e.getMethod());
        Result<?> result = Result.fail(ResultCode.METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(result, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * 处理认证相关异常的方法。当捕获指定的认证异常时，返回统一封装的错误响应。
     *
     * @param e 捕获到的异常对象，可能是 {@code BadCredentialsException}、{@code UsernameNotFoundException} 或
     *          {@code InternalAuthenticationServiceException}，包含具体的异常信息。
     * @return 一个 {@code ResponseEntity} 对象，其中包含封装的错误信息 {@code Result} 实例，状态码为 401。
     */
    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class, InternalAuthenticationServiceException.class})
    public ResponseEntity<Result<?>> handleAuthenticationException(Exception e){
        log.warn("登录失败: {}", e.getMessage());
        Result<?> result = Result.fail(ResultCode.UNAUTHORIZED);
        return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 处理方法参数校验异常的方法。当抛出 {@code MethodArgumentNotValidException} 时，
     * 此方法会捕获异常并提取所有校验错误的信息，拼接形成错误提示。
     *
     * @param e 捕获到的 {@code MethodArgumentNotValidException} 异常对象，包含校验失败的详细信息。
     * @return 一个 {@code ResponseEntity} 对象，其中包含封装的错误信息 {@code Result} 实例，状态码为 400。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<?>> handleValidationException(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder("参数校验异常：");

        for(FieldError error : bindingResult.getFieldErrors()){
            sb.append(error.getField())
              .append(":")
              .append(error.getDefaultMessage())
              .append(";");
        }

        String message = sb.toString();
        log.warn("参数校验异常: {}", message);
        Result<?> result = Result.fail(ResultCode.BAD_REQUEST, message);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理访问权限不足异常的方法。当捕获到 AccessDeniedException 时，
     * 此方法会对异常进行统一处理，返回格式化的错误响应。
     *
     * @param e 捕获到的 AccessDeniedException 异常对象，包含详细的权限错误信息。
     * @return 一个 ResponseEntity 对象，包含封装好的错误信息 Result 实例，状态码为 403。
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Result<?>> handleAccessDeniedException(AccessDeniedException e){
        log.warn("权限不足: {}", e.getMessage());
        Result<?> result = Result.fail(ResultCode.FORBIDDEN);
        return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
    }

    /**
     * 处理系统中未捕获的通用异常的方法。
     * 当发生未明确处理的 Exception 时，此方法会统一捕获并返回标准错误响应。
     *
     * @param e 捕获到的 Exception 对象，包含异常的具体信息和堆栈信息。
     * @return 一个 ResponseEntity 对象，包含封装的错误信息 Result 实例，状态码为 500。
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<?>> handleException(Exception e){
        log.error("系统异常: ", e); // 打印堆栈信息以便排查
        Result<?> result = Result.fail(ResultCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
