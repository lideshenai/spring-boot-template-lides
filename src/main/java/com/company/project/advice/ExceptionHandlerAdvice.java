package com.company.project.advice;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;

import io.jsonwebtoken.ExpiredJwtException;



/**
 * springmvc异常处理
 * 
 * @author 小威老师
 *未使用
 */
//@RestControllerAdvice
public class ExceptionHandlerAdvice {

	private static final Logger log = LoggerFactory.getLogger("adminLogger");

	@ExceptionHandler({ IllegalArgumentException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Result badRequestException(IllegalArgumentException exception) {
		System.out.println("非法参数异常");
		return ResultGenerator.genFailResult(exception.getMessage());
		//return new ResponseInfo(HttpStatus.BAD_REQUEST.value() + "", exception.getMessage());
	}

	@ExceptionHandler({ AccessDeniedException.class })
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public Result badRequestException(AccessDeniedException exception) {
		System.out.println("权限不足");
		return ResultGenerator.genFailResult("权限不足");
	}
	/*@ExceptionHandler({ ExpiredJwtException.class })
	public Result badRequestException(ExpiredJwtException exception) {
		return ResultGenerator.genFailResult("登录过期");
	}*/

	@ExceptionHandler({ MissingServletRequestParameterException.class, HttpMessageNotReadableException.class,
			UnsatisfiedServletRequestParameterException.class, MethodArgumentTypeMismatchException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Result badRequestException(Exception e,HttpServletRequest request) {
		System.out.println("请求异常");
		String message = "";
         if (e instanceof NoHandlerFoundException) {
            message = "接口 [" + request.getRequestURI() + "] 不存在";
        } else if (e instanceof ServletException) {
            message = e.getMessage();
        } 
            log.error(message, e);
		return ResultGenerator.genFailResult(message);
	}

	/*@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Result exception(Throwable e,HttpServletRequest request,HandlerMethod handler) {
		System.out.println("系统异常");
		log.error("系统异常", e);
		HandlerMethod handlerMethod = (HandlerMethod) handler;
       String message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s",
                request.getRequestURI(),
                handlerMethod.getBean().getClass().getName(),
                handlerMethod.getMethod().getName(),
                e.getMessage());
		return ResultGenerator.genFailResult(message);

	}*/

}
