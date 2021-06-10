package com.drop.leaves.jooqhub.module.userinfo.model;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author mbs on 2021-06-10 17:08
 */
@Data
@Accessors(chain = true)
@ToString
public class UserInfoModel {

    private Integer id;
    private String name;
    private Integer age;
    private String addr;
}
