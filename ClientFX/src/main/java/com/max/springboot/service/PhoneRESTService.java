package com.max.springboot.service;

import com.max.springboot.domain.Phone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;


/** Injects builder, retrieve with Mono, Flux wrappers. Provides blocking, non-blocking modes.
    Consume REST service with retrieve(). Use block() for synchronous retrieves. */
@Service
public class PhoneRESTService{

    private final WebClient webClient;
    private static final Logger log = LoggerFactory.getLogger( PhoneRESTService.class );


    public PhoneRESTService( WebClient.Builder webClientBuilder ){
        webClient = webClientBuilder.baseUrl( "http://localhost:8080" ).build();
    }

    public List<Phone> getPhones(){

        // To test the client without the server use this block
        /* List<Phone> result = new ArrayList<>();
        result.add( Phone.newTestPhone( 1, false, "Samsung Galaxy S9", "Guiseppe" ) );
        result.add( Phone.newTestPhone( 2, false, "Motorola Nexus 6", "Viacheslav"  ) );
        result.add( Phone.newTestPhone( 3, false, "Apple iPhone 13", "Max" ) );
        result.add( Phone.newTestPhone( 4, true, "Nokia 3310", ""  ) );*/

        List<Phone> result = webClient.get()
            .uri( "/phones" )
            .retrieve()
            .bodyToFlux( Phone.class )
            .collectList()
            .block();
        result.forEach( phone -> log.info( phone.toString() ) );

        return result;

    }

    public Phone getPhone( Integer id ){

        Phone phone = webClient.get().uri( "/{id}/", id )
            .retrieve()
            .bodyToMono( Phone.class )
            .block();
        log.info( phone + " read." );
        return phone;

    }

    public Phone updatePhone( Phone phone1 ){

        Phone phone = webClient.put()
            .uri( "/phones/" + phone1.getId() )
            .body( Mono.just( phone1 ), Phone.class )
            .retrieve()
            .bodyToMono( Phone.class )
            .block();

        log.info( phone.getName() + ( phone.isAvailable() ? " returned" :
            ( " booked by " + phone.getBookedBy() + " at " + phone.getDateBooked() ) ) );

        return phone;

    }

}
