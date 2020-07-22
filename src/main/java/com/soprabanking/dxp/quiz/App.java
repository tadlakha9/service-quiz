package com.soprabanking.dxp.quiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.soprabanking.dxp.commons.constants.DxpConstants.DXP_BASE_PACKAGE;

@SpringBootApplication(scanBasePackages = DXP_BASE_PACKAGE, proxyBeanMethods = false)
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);

    }
}
