package com.max.springbootrest;

import com.max.springbootrest.domain.Phone;
import com.max.springbootrest.service.PhoneRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


//@RunWith( SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ServerRestApplication.class)
public class SpringBootJPATest{

    @Autowired
    private PhoneRepository genericEntityRepository;

    @Test
    public void repository_whenSaveAndRetreiveEntity_thenOK() {

        Phone genericEntity = genericEntityRepository.save( new Phone( "test" ) );
        Phone foundedEntity = genericEntityRepository.findByPhoneName( "test" );

        assertNotNull( foundedEntity );
        assertEquals( genericEntity.getName(), foundedEntity.getName() );

    }

}
