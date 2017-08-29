package com.bstek.urule.springboot;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.bstek.urule.console.servlet.URuleServlet;

/**
 * @author Jacky.gao
 * @since 2016年10月12日
 */
@Component
public class URuleServletRegistration {
	@Bean
	public ServletRegistrationBean registerURuleServlet(){
		return new ServletRegistrationBean(new URuleServlet(),"/urule/*");
	}
	@Bean
	public ServletRegistrationBean registerIndexServlet(){
		return new ServletRegistrationBean(new IndexServlet(),"/");
	}
	
}
