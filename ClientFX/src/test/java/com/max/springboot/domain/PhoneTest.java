package com.max.springboot.domain;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Date;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;


public class PhoneTest{

    @Test
    public void testNewTestPhone(){

        final Phone phone = Phone.newTestPhone( 10, false, "iPhone 13", "John Doe" );

        assertEquals( 10, phone.getId().intValue() );
        assertFalse( phone.isAvailable() );
        assertEquals( "iPhone 13", phone.getName() );
        assertEquals( "John Doe", phone.getBookedBy() );
        assertNotNull( phone.getDateBooked() );

    }

    @Test
    public void testSetName(){

        Phone phone = Phone.newTestPhone( 1, false, "iPhone 13", "John Doe" );
        phone.setName( "Samsung Galaxy S21" );
        assertEquals( "Samsung Galaxy S21", phone.getName() );

    }

    @Test
    public void testPhoneAvailability() {

        Phone phone = Phone.newTestPhone( 1, true, "Test Phone", null );
        Assert.assertTrue( phone.isAvailable(), "Phone should be available" );
        Assert.assertNull( phone.getBookedBy(), "Booked by name should be null" );
        Assert.assertNull( phone.getDateBooked(), "Date booked should be null" );

        phone.setAvailable( false );
        phone.setBookedBy( "Jane Smith" );
        final Date dateBooked = new Date();
        phone.setDateBooked( dateBooked );

        Assert.assertFalse( phone.isAvailable(), "Phone should be booked" );
        Assert.assertEquals( phone.getBookedBy(), "Jane Smith", "Incorrect booked by name" );
        Assert.assertEquals( phone.getDateBooked().getTime(), dateBooked.getTime(), "" );

    }

}