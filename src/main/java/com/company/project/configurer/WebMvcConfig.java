package com.company.project.configurer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.ResourceUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.company.project.core.Result;
import com.company.project.core.ResultCode;
import com.company.project.core.ServiceException;
import com.company.project.page.table.PageTableArgumentResolver;



@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	private static final Logger log = LoggerFactory.getLogger(WebMvcConfig.class);

	/**
	 * 跨域支持
	 * 
	 * @return
	 */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedMethods("*");
			}
		};
	}
    //使用阿里 FastJson 作为JSON MessageConverter
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter=new FastJsonHttpMessageConverter();
    	//FastJsonHttpMessageConverter4 converter = new FastJsonHttpMessageConverter4();
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.WriteMapNullValue,//保留空的字段
                //SerializerFeature.WriteNullStringAsEmpty,//String null -> ""
                //SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.DisableCircularReferenceDetect);//禁止循环引用
        converter.setFastJsonConfig(config);
        converter.setDefaultCharset(Charset.forName("UTF-8"));
        converters.add(converter);
    }
  //统一异常处理
    @Override
      public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {   	
      	exceptionResolvers.add(new HandlerExceptionResolver() {
              public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
                  Result result = new Result();
                  if (e instanceof ServiceException) {//业务失败的异常，如“账号或密码错误”
                      result.setCode(ResultCode.FAIL).setMessage(e.getMessage());
                      log.info(e.getMessage());
                  } else if (e instanceof NoHandlerFoundException) {
                      result.setCode(ResultCode.NOT_FOUND).setMessage("接口 [" + request.getRequestURI() + "] 不存在");
                  } else if (e instanceof ServletException) {
                      result.setCode(ResultCode.FAIL).setMessage(e.getMessage());
                  } else {
                      result.setCode(ResultCode.INTERNAL_SERVER_ERROR).setMessage("接口 [" + request.getRequestURI() + "] 内部错误");
                      String message;
                      if (handler instanceof HandlerMethod) {
                          HandlerMethod handlerMethod = (HandlerMethod) handler;
                          message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s",
                                  request.getRequestURI(),
                                  handlerMethod.getBean().getClass().getName(),
                                  handlerMethod.getMethod().getName(),
                                  e.getMessage());
                      } else {
                          message = e.getMessage();
                      }
                      log.error(message, e);
                  }
                  responseResult(response, result);
                  return new ModelAndView();
              }

          });
      }
    private void responseResult(HttpServletResponse response, Result result) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(200);
        try {
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

	/**
	 * datatable分页解析
	 * 
	 * @return
	 */
	@Bean
	public PageTableArgumentResolver tableHandlerMethodArgumentResolver() {
		return new PageTableArgumentResolver();
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(tableHandlerMethodArgumentResolver());
	}

	/**
	 * 上传文件根路径
	 */
	@Value("${files.path}")
	private String filesPath;

	/**
	 * 外部文件访问
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/statics/**")
				.addResourceLocations(ResourceUtils.FILE_URL_PREFIX + filesPath + File.separator);
	}

}
