package com.max.springboot;

import com.gluonhq.ignite.spring.SpringContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;


/** Main application client class.

 By using Gluon Ignite, you get dependency injection inside your application class and within FXML
 controllers. Gluon Ignite is used for integration. Spring Boot separates UI from backend operations. */
@SpringBootApplication
@ComponentScan({
    "com.gluonhq.ignite.spring",
    "com.max.springboot"
})
public class PhoneClientApp extends Application implements PhoneFXApp{

    @Autowired
    private FXMLLoader loader;
    private final SpringContext context = new SpringContext( this );
    private ConfigurableApplicationContext ctx;

    @Override
    public void start( Stage stage ) throws IOException{

        context.init( () -> {
            return ctx = SpringApplication.run( PhoneClientApp.class );
        } );
        loader.setLocation( getViewLocation() );

        final Parent primaryView = loader.load();
        stage.setTitle( "Phone Booking Demo - Spring Boot Ignite with REST MultiThread" );
        stage.setScene( new Scene( primaryView ) );
        stage.show();

    }

    @Override
    public void stop(){
        // Close this application context, destroys all beans in its bean factory
        ctx.close();
    }

    public static void main( String[] args ){
        Application.launch( PhoneClientApp.class, args );
    }

}
