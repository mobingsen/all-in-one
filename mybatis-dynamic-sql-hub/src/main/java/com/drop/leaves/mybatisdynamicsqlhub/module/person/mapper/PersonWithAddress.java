package com.drop.leaves.mybatisdynamicsqlhub.module.person.mapper;

import com.drop.leaves.mybatisdynamicsqlhub.module.person.model.AddressRecord;
import com.drop.leaves.mybatisdynamicsqlhub.module.person.model.LastName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author mobingsen
 */
@Data
@Accessors(chain = true)
public class PersonWithAddress {

    private Integer id;
    private String firstName;
    private LastName lastName;
    private Date birthDate;
    private Boolean employed;
    private String occupation;
    private AddressRecord address;
}
