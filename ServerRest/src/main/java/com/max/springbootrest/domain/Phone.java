package com.max.springbootrest.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "phones")
public class Phone implements Serializable {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Basic( optional = false )
    @Column( name = "id" )
    private Integer _Id;

    @Column(name = "name")
    private String _Name;

    @Column(name = "available")
    private Boolean _Available;

    @Column(name = "bookedby")
    private String _BookedBy;

    @Column(name = "datebooked")
    private Date _DateBooked;


    public Phone(){
    }

    public Integer getId(){
        return _Id;
    }

    public void setId( Integer id ){
        this._Id = id;
    }

    public String getName(){
        return _Name;
    }

    public void setName( String name ){
        this._Name = name;
    }

    public Boolean getAvailable(){
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
        return _Id + ": " + _Name;
    }

}
