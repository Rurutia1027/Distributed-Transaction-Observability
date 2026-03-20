package org.tus.demo.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class OrderDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderDemoApplication.class, args);
    }
}
