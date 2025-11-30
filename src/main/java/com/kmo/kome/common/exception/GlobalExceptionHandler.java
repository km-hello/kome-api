package com.kmo.kome.common.exception;

import com.kmo.kome.common.Result;
import com.kmo.kome.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

/**
 * 全局异常处理器，用于捕获和处理系统运行过程中出现的各种异常。
 * 通过集中处理异常，可以提供统一的错误响应格式，提高系统的可维护性和用户体验。
 * 支持处理身份认证、业务逻辑、参数校验、权限不足以及未知异常等场景。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理身份认证相关异常的方法。当捕获到 BadCredentialsException、UsernameNotFoundException
     * 或 InternalAuthenticationServiceException 异常时，统一返回未授权错误响应。
     *
     * @param e 捕获到的异常对象，表示具体的身份认证失败原因。
     * @return 一个 {@code Result} 实例，包含错误码 401 和提示消息 "用户名或密码错误"。
     */
    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class, InternalAuthenticationServiceException.class})
    public Result<?> handleAuthenticationException(Exception e){
        log.warn("登录失败: {}", e.getMessage());
        return Result.fail(ResultCode.UNAUTHORIZED.getCode(), "用户名或密码错误");
    }

    /**
     * 处理业务异常的方法。当抛出 ServiceException 时，此方法会捕获异常并统一返回标准的错误响应。
     *
     * @param e 捕获到的 ServiceException，包含具体的错误码和错误信息。
     * @return 统一封装的错误响应对象，包含错误码和错误信息。
     */
    @ExceptionHandler(ServiceException.class)
    public Result<?> handleServiceException(ServiceException e){
        log.error("业务异常: code={}, message={}", e.getCode(), e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常的方法。当抛出 MethodArgumentNotValidException 时，
     * 此方法会捕获异常并返回统一的错误响应，包括详细的字段错误信息。
     *
     * @param e 捕获到的 MethodArgumentNotValidException，包含校验失败的详细信息。
     * @return 统一封装的错误响应对象，包含错误码 400 和校验失败的详细信息。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidationException(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder("参数校验失败：");

        for(FieldError error : bindingResult.getFieldErrors()){
            sb.append(error.getField())
              .append(":")
              .append(error.getDefaultMessage())
              .append(";");
        }

        String message = sb.toString();
        log.warn("参数校验异常: {}", message);
        return Result.fail(ResultCode.VALIDATE_FAILED.getCode(), message);
    }

    /**
     * 处理权限不足异常的方法。当抛出 AccessDeniedException 时，
     * 此方法会捕获异常并返回统一的错误响应。
     *
     * @param e 捕获到的 AccessDeniedException，包含权限不足的详细信息。
     * @return 统一封装的错误响应对象，包含错误码 403 和提示的错误信息。
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result<?> handleAccessDeniedException(AccessDeniedException e){
        log.warn("权限不足: {}", e.getMessage());
        return Result.fail(ResultCode.FORBIDDEN.getCode(), "您没有权限执行此操作");
    }


    /**
     * 处理系统未知异常的方法。当发生未捕获的异常时，此方法会执行统一处理，
     * 返回标准的错误响应，提示系统繁忙。
     *
     * @param e 捕获到的异常对象，包含具体的堆栈信息和异常描述。
     * @return 一个 {@code Result} 实例，包含错误码 500 和提示信息 "系统繁忙，请稍后重试"。
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e){
        log.error("系统未知异常: ", e); // 打印堆栈信息以便排查
        return Result.fail(ResultCode.FAILED.getCode(), "系统繁忙，请稍后重试");
    }
}
