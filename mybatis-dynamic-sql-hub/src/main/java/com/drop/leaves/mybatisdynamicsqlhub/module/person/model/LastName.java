package com.drop.leaves.mybatisdynamicsqlhub.module.person.model;

import lombok.*;

/**
 * @author mobingsen
 */
@Data
public class LastName {

    private String name;

    public static LastName of(String name) {
        LastName lastName = new LastName();
        lastName.setName(name);
        return lastName;
    }
}