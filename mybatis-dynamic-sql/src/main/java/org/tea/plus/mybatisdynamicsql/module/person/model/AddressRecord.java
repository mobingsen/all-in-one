package org.tea.plus.mybatisdynamicsql.module.person.model;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author mobingsen
 */
@Data
@Accessors(chain = true)
public class AddressRecord {

    private Integer id;
    private String streetAddress;
    private String city;
    private String state;
}
