package com.company.project.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Token过滤器
 * 
 * @author 小威老师
 *
 *         2017年10月14日
 */
@Component
public class TokenFilter extends OncePerRequestFilter {

	private static final String TOKEN_KEY = "token";

	
	@Autowired
	private UserDetailsService userDetailsService;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//获取token，如果有进行校验，通过就创建：UsernamePasswordAuthenticationToken
		String token = getToken(request);
		if (StringUtils.isNotBlank(token)) {
			String username = (String)JavaWebToken.parserJavaWebToken(token).get("user");
			LoginUser loginUser = (LoginUser)userDetailsService.loadUserByUsername(username);
			if (loginUser != null) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginUser,
						null, loginUser.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		//调用UsernamePasswordAuthenticationFilter
		filterChain.doFilter(request, response);
	}



	/**
	 * 根据参数或者header获取token
	 * 
	 * @param request
	 * @return
	 */
	public static String getToken(HttpServletRequest request) {
		String token = request.getParameter(TOKEN_KEY);
		if (StringUtils.isBlank(token)) {
			token = request.getHeader(TOKEN_KEY);
		}

		return token;
	}

}
