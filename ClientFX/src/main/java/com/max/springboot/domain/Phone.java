package com.max.springboot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.Objects;


@JsonIgnoreProperties( ignoreUnknown = true )
public class Phone{

    private Integer _Id;
    private String _Name;
    private Boolean _Available;
    private String _BookedBy;
    private Date _DateBooked;

    public Phone(){
    }

    public static Phone newTestPhone( int id, boolean available, String name, String bookedBy ){

        final Phone result = new Phone();
        result._Id = id;
        result._Name = name;
        result.setAvailable( available );

        if( !available ){
            result.setBookedBy( bookedBy );
            result.setDateBooked( new Date() );
        }
        else{
            assert( null == bookedBy );
        }

        return result;

    }


    public Integer getId(){
        return _Id;
    }

    public void setId( Integer id ){
        _Id = id;
    }

    public String getName(){
        return _Name;
    }

    public void setName( String name ){
        this._Name = name;
    }

    public Boolean isAvailable(){
        return _Available;
    }

    public void setAvailable( Boolean available ){
        _Available = available;
    }

    public String getBookedBy(){
        return _BookedBy;
    }

    public void setBookedBy( String bookedBy ){
        _BookedBy = bookedBy;
    }

    public Date getDateBooked(){
        return _DateBooked;
    }

    public void setDateBooked( Date dateBooked ){
        _DateBooked = dateBooked;
    }

    @Override
    public String toString(){
        return _Id + ": " + _Name + " - " + ( _Available ? "Available" : "Booked by " + _BookedBy );
    }

    @Override
    public boolean equals( Object o ){

        if( this == o ){
            return true;
        }
        if( o == null || getClass() != o.getClass() ){
            return false;
        }

        final Phone phone = ( Phone )o;
        return _Id.equals( phone._Id );

    }

    @Override
    public int hashCode(){
        return Objects.hash( _Id );
    }

}
