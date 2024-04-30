package com.max.springbootrest.service;


public class PhoneNotFoundException extends RuntimeException{

    public PhoneNotFoundException( String exception ){
        super( exception );
    }

}
