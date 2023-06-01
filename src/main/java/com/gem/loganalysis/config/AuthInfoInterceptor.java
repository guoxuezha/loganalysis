package com.gem.loganalysis.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gem.loganalysis.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * 全局登录拦截
 *
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/19 15:04
 */
@Component
@Slf4j
public class AuthInfoInterceptor implements HandlerInterceptor {

    @Value("${verifyUrl}")
    private String verifyUrl;

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        String userId = request.getHeader("userId");
        String token = request.getHeader("token");
        if (StrUtil.isEmpty(userId) || StrUtil.isEmpty(token)) {
            verifyFailResponse(response, "缺失认证信息");
            return false;
        }
        HashMap<String, Object> param = new HashMap<>(8);
        param.put("userId", userId);
        param.put("accessToken", token);
        String verifyResult = HttpUtil.post(verifyUrl, param);
        log.info("verifyResult : {}", verifyResult);
        String code;
        try {
            code = (String) JSONUtil.parseObj(verifyResult).get("code");
        } catch (Exception e) {
            verifyFailResponse(response, e.getMessage());
            return false;
        }
        // 注意 这里必须是true否则请求将就此终止。
        if ("0000".equals(code)) {
            return true;
        } else {
            verifyFailResponse(response, "认证失败!");
            return false;
        }
    }

    /**
     * 认证失败返回报文
     *
     * @param response response
     * @param message  响应信息
     */
    private void verifyFailResponse(HttpServletResponse response, String message) {
        response.addHeader("Content-Type", "application/json;charset=UTF-8");
        try {
            response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(Result.failed(message)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
