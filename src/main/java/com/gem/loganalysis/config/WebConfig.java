package com.gem.loganalysis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/19 15:20
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInfoInterceptor authInfoInterceptor;

    public WebConfig(AuthInfoInterceptor authInfoInterceptor) {
        this.authInfoInterceptor = authInfoInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInfoInterceptor).addPathPatterns("/**");
    }

}