package com.gem.loganalysis.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/2")
    public Integer test2(@RequestParam("id") Integer id) {
//        return AssetTypeTree.getInstance().getParentNodeId(id);
        return null;
    }

}
