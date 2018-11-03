package com.leaf.job.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter{
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
       
    	registry.addResourceHandler(                
                "/img/**",
                "/css/**",
                "/js/**")
                .addResourceLocations(                        
                		"classpath:/static/css/config/images",
                        "classpath:/static/css/config",
                        "classpath:/static/js/",
                        "classpath:/static/js/config");
    }
}
