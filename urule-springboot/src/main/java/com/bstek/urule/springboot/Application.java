package com.bstek.urule.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author Jacky.gao
 * @since 2016年10月12日
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@ImportResource({"classpath:urule-console-context.xml"})
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class,args);
	}
	
	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
	    TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
	    return tomcat;
	}
}
