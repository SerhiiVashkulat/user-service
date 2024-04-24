package ua.vahskulat.model;


import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Address {
    private String country;
    private String city;
    private String street;
    private String streetNumber;
    private String apartment;
    private String zip;

}
