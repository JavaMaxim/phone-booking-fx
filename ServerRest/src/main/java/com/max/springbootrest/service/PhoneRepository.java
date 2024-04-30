package com.max.springbootrest.service;

import com.max.springbootrest.domain.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PhoneRepository extends JpaRepository<Phone, Integer> {

    @Query(
        nativeQuery = true, value = "select * from phones where name = :name limit 1"
    )
    Phone findByPhoneName(@Param("name") String name);

}
