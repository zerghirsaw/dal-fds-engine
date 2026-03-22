package com.digdaya.dal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Runner untuk DAL-FDS Core Gateway & UI
 */
@SpringBootApplication
public class DalApplication {

    public static void main(String[] args) {
        SpringApplication.run(DalApplication.class, args);
        System.out.println("\n🚀 DAL-FDS Core Gateway & Vaadin UI Berhasil Dinyalakan!\n");
    }

}
