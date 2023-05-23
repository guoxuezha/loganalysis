package com.gem.loganalysis.config;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 参数解密过滤器
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/22 10:03
 */
@ControllerAdvice
public class ParamDecryptAdvice implements RequestBodyAdvice {

    @Resource
    private BusinessConfigInfo businessConfigInfo;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return businessConfigInfo.getResponseEncryptEnable();
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return new DecryptHttpInputMessage(inputMessage, businessConfigInfo.getAESKey());
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}
