package com.drop.leaves.mybatisdynamicsqlhub.module.person.model;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author mobingsen
 */
@Data
@Accessors(chain = true)
public class LastName {

    private String name;

    public static LastName of(String name) {
        LastName lastName = new LastName();
        lastName.setName(name);
        return lastName;
    }
}
