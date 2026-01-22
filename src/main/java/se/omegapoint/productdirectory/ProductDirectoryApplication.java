package se.omegapoint.productdirectory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductDirectoryApplication {
    private static final Logger log = LoggerFactory.getLogger(ProductDirectoryApplication.class);


    public static void main(String[] args) {

        SpringApplication.run(ProductDirectoryApplication.class, args);
        log.info("Application running");

    }

}
