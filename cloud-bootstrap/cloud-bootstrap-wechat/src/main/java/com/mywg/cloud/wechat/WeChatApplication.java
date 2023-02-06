package com.mywg.cloud.wechat;

import com.mywg.base.common.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 启动入口
 * @author by linqiaoguo
 * @Date 2023/2/3 11:04
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {Constants.BASE_PACKAGE})
public class WeChatApplication {

    public static void main(String[] args) {

        SpringApplication.run(WeChatApplication.class, args);
    }
}
