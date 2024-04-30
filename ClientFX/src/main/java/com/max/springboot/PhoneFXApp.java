package com.max.springboot;

import java.net.URL;


public interface PhoneFXApp{

    default URL getViewLocation(){
        return getClass().getResource( "view.fxml" );
    }

}
