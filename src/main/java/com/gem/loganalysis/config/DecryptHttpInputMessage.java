package com.gem.loganalysis.config;


import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.gem.utils.crypto.AES;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/22 10:06
 */
public class DecryptHttpInputMessage implements HttpInputMessage {

    private final HttpInputMessage inputMessage;

    private final String AESKey;

    public DecryptHttpInputMessage(HttpInputMessage inputMessage, String key) {
        this.inputMessage = inputMessage;
        this.AESKey = key;
    }

    @Override
    public InputStream getBody() throws IOException {
        String content = IoUtil.read(inputMessage.getBody(), Charset.defaultCharset());
        String param = new JSONObject(content).getStr("param");
        return new ByteArrayInputStream(JSONUtil.toJsonStr(AES.decryptAES(param, AESKey)).getBytes());
    }

    @Override
    public HttpHeaders getHeaders() {
        return inputMessage.getHeaders();
    }
}