package org.tea.plus.mybatisdynamicsql.module.person.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author mobingsen
 */
@Data
@Accessors(chain = true)
@Entity
@Table
public class PersonRecord {

    @Id
    private Integer id;
    @Column
    private String firstName;
    private transient LastName lastName;
    @Column
    private Date birthDate;
    @Column
    private Boolean employed;
    @Column
    private String occupation;
    @Column
    private Integer addressId;
}
