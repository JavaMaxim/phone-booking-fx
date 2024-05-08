package com.max.springbootrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/** Main application class for the REST server. */
@SpringBootApplication
public class ServerRestApplication{

    public static void main(String[] args) {
        SpringApplication.run( ServerRestApplication.class, args);
    }

}
