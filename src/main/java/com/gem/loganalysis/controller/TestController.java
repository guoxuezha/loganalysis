package com.gem.loganalysis.controller;

import com.gem.loganalysis.model.bo.LogEventListener;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author GuoChao
 * @version 1.0
 * @date 2023/5/26 0:06
 */
@RestController
@RequestMapping("/test")
@AllArgsConstructor
public class TestController {

    private final LogEventListener logEventListener;

    @PostMapping("/1")
    public void test1() {

    }

}
