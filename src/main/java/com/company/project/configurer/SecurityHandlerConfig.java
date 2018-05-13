package com.company.project.configurer;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.alibaba.fastjson.JSON;
import com.company.project.auth.JavaWebToken;
import com.company.project.auth.LoginUser;
import com.company.project.core.Result;
import com.company.project.core.ResultCode;
import com.company.project.utils.ResponseUtil;
import com.company.project.utils.UserUtil;

/**
 * spring security处理器
 * 
 * @author 小威老师
 *
 *         2017年10月16日
 */
@Configuration
public class SecurityHandlerConfig {
	
	private static final Logger log = LoggerFactory.getLogger(SecurityHandlerConfig.class);
	@Resource
	JavaWebToken javaWebToken;
	/**
	 * 登陆成功，返回Token
	 * 
	 * @return
	 */
	@Bean
	public AuthenticationSuccessHandler loginSuccessHandler() {
		return new AuthenticationSuccessHandler() {
			
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				LoginUser user = UserUtil.getLoginUser();
				String token = javaWebToken.createJavaWebToken(user.getUsername());
				Result result = new Result();
				result.setCode(ResultCode.SUCCESS);
				result.setData(token);
				ResponseUtil.responseResult(response, result);
			}
		};
	}

	/**
	 * 登陆失败
	 * 
	 * @return
	 */
	@Bean
	public AuthenticationFailureHandler loginFailureHandler() {
		System.out.println("登录失败");
		return new AuthenticationFailureHandler() {
			
			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException exception) throws IOException, ServletException {
				  log.warn("签名认证失败，请求接口：{}，请求IP：{}，请求参数：{}",
                          request.getRequestURI(), getIpAddress(request), JSON.toJSONString(request.getParameterMap()));
				Result result = new Result();
				String msg = exception.getMessage();
				if("Bad credentials".equals(msg))msg="密码错误";
                result.setCode(ResultCode.UNAUTHORIZED).setMessage(msg);
				ResponseUtil.responseResult(response, result);
				
			}
		};
	}

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果是多级代理，那么取第一个ip为客户端ip
        if (ip != null && ip.indexOf(",") != -1) {
            ip = ip.substring(0, ip.indexOf(",")).trim();
        }

        return ip;
    }
	/**
	 * 未登录，返回401
	 * 
	 * @return
	 */
	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		System.out.println("未登录");
		return new AuthenticationEntryPoint() {
			
			@Override
			public void commence(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException exception) throws IOException, ServletException {
			
				Result result = new Result();
				 result.setCode(ResultCode.UNAUTHORIZED).setMessage(exception.getMessage());
               // result.setCode(ResultCode.UNAUTHORIZED).setMessage("未登录");
				ResponseUtil.responseResult(response, result);
				
			}
		};
	}

	/**
	 * 退出处理
	 * 
	 * @return
	 */
	@Bean
	public LogoutSuccessHandler logoutSussHandler() {
		return new LogoutSuccessHandler() {
			
			@Override
			public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
					throws IOException, ServletException {
				Result result = new Result();
				result.setCode(ResultCode.SUCCESS);
				result.setData("退出成功，请清除token");
				ResponseUtil.responseResult(response, result);
				
			}
		};

	}

}
