package com.bidsphere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BidSphereApplication {
    public static void main(String[] args) { SpringApplication.run(BidSphereApplication.class, args); }
}
