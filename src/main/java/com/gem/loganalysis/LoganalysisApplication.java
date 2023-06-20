package com.gem.loganalysis;

import cn.hutool.core.thread.ThreadUtil;
import com.gem.loganalysis.handler.ConsoleFilterInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@SpringBootApplication(scanBasePackages = "com.gem.loganalysis")
@EnableScheduling
@Slf4j
public class LoganalysisApplication {

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.setOut(new PrintStream(new ConsoleFilterInterceptor(), true, StandardCharsets.UTF_8.name()));

        SpringApplication.run(LoganalysisApplication.class, args);
        ThreadUtil.execute(() -> {
            ThreadUtil.sleep(1, TimeUnit.SECONDS); // 延迟 1 秒，保证输出到结尾
            log.info("\n----------------------------------------------------------\n\t" +
                    "项目启动成功！\n" +
                    "----------------------------------------------------------");
        });
    }

}
