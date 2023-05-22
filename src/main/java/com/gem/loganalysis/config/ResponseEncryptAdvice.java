package com.gem.loganalysis.config;

import cn.hutool.json.JSONUtil;
import com.gem.utils.crypto.AES;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashMap;

/**
 * 报文加密过滤器
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/22 9:16
 */
@ControllerAdvice
public class ResponseEncryptAdvice implements ResponseBodyAdvice {

    @Value("${AESKey}")
    private String AESKey;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        // 所有接口全都加密
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        HashMap<String, String> newResponse = new HashMap<>(2);
        newResponse.put("response", AES.encryptAES(JSONUtil.toJsonStr(body), AESKey));
        return newResponse;
    }

}
