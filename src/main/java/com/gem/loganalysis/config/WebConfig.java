package com.gem.loganalysis.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/19 15:20
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInfoInterceptor authInfoInterceptor;

    @Resource
    private BusinessConfigInfo businessConfigInfo;

    public WebConfig(AuthInfoInterceptor authInfoInterceptor) {
        this.authInfoInterceptor = authInfoInterceptor;
    }

    @Override
    public void addInterceptors(@NotNull InterceptorRegistry registry) {
        if (businessConfigInfo.getAuthenticationEnable()) {
            registry.addInterceptor(authInfoInterceptor)
                    .addPathPatterns("/sop/**")
                    .addPathPatterns("/logAnalysis/**")
                    .addPathPatterns("/system/**")
                    .excludePathPatterns("/websocket/**");
        }
    }

}