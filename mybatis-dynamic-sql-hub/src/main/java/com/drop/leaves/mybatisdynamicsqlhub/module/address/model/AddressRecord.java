package com.drop.leaves.mybatisdynamicsqlhub.module.address.model;

import lombok.*;

/**
 * @author mobingsen
 */
@Data
public class AddressRecord {

    private Integer id;
    private String streetAddress;
    private String city;
    private String state;
}