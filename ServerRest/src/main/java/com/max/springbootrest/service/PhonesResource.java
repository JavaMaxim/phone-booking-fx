package com.max.springbootrest.service;

import com.max.springbootrest.domain.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


/** Phones REST resource it is a producer/consumer with Service resources. */
@RestController
public class PhonesResource{

    @Autowired
    private PhoneRepository _Repository;

    @GetMapping("/phones")
    public List<Phone> retrieveAllPhones() {
        return _Repository.findAll();
    }

    @GetMapping( "/phones/{id}" )
    public Phone retrievePhone( @PathVariable Integer id ){

        Optional<Phone> phoneOptional = _Repository.findById( id );
        if( !phoneOptional.isPresent() ){
            throw new PhoneNotFoundException( "id: " + id );
        }

        return phoneOptional.get();

    }

    @DeleteMapping( "/phones/{id}" )
    public void deletePhone( @PathVariable Integer id ){
        _Repository.deleteById( id );
    }

    @PostMapping("/phones")
    public ResponseEntity<Object> createPhone( @RequestBody Phone phone ){
        Phone savedPhone = _Repository.save( phone );
        System.out.println( "Created: " + savedPhone );
        return new ResponseEntity<>( savedPhone, HttpStatus.CREATED );
    }

    @PutMapping("/phones/{id}")
    public ResponseEntity<Object> updatePhone( @RequestBody Phone phone, @PathVariable Integer id ){

        Optional<Phone> phoneOptional = _Repository.findById( id );
        if( !phoneOptional.isPresent() ){
            return ResponseEntity.notFound().build();
        }

        phone.setId( id );
        Phone updatedPhone = _Repository.save( phone );
        System.out.println( "Updated: " + updatedPhone );
        return new ResponseEntity<>( updatedPhone, HttpStatus.OK );

    }

}
