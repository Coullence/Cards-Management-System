package com.google.cms.utilities.requests_proxy;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<RequestInterceptor> userNameFilterRegistrationBean() {
        FilterRegistrationBean<RequestInterceptor> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestInterceptor());
        registrationBean.addUrlPatterns("/*"); // Apply the filter to all URLs
        return registrationBean;
    }
}
