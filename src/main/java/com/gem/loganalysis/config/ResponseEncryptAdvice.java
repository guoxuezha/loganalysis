package com.gem.loganalysis.config;

import cn.hutool.json.JSONUtil;
import com.gem.loganalysis.util.AESUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;
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

    @Resource
    private BusinessConfigInfo businessConfigInfo;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        // 根据响应类型对报文加密
        if (businessConfigInfo.getResponseEncryptEnable()) {
            String typeName = returnType.getGenericParameterType().getTypeName();
            return typeName.startsWith("com.gem.loganalysis.model.Result");
        } else {
            return false;
        }
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        HashMap<String, String> newResponse = new HashMap<>(2);
        newResponse.put("response", AESUtil.aesEncrypt(JSONUtil.toJsonStr(body), businessConfigInfo.getAESKey()));
        return newResponse;
    }

}
