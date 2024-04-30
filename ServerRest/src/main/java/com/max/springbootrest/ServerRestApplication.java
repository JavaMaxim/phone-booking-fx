package com.max.springbootrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
/** Main application class for the REST server. */
public class ServerRestApplication{

    public static void main(String[] args) {
        SpringApplication.run( ServerRestApplication.class, args);
    }

}
