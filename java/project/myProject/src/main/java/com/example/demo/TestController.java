package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 11:37 2018/12/18
 * @Modified By:
 */
@RestController
@Slf4j
public class TestController {
    @GetMapping("/hello")
    public String test() {
        test0();
        return "hello";
    }

    private void test0() {
        log.info("wangkang");
    }
}
