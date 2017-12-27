package com.studentAssist.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.studentAssist.interceptor.ExecuteInterceptor;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Autowired
	ExecuteInterceptor interceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		// registry.addInterceptor(interceptor);

		registry.addInterceptor(interceptor).addPathPatterns("/profile/*");

	}
}