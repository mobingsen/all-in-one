package org.tea.plus.hdp.config;

import org.springframework.boot.SpringApplication;

/**
 * @author mbs on 2021/5/26 18:33
 */
public final class AppRun {

    private AppRun() {
    }

    public static void run(Class<?> primarySource, String[] args) {
        SpringApplication.run(primarySource, args);
        new HdpBanner().printBanner();
    }
}
