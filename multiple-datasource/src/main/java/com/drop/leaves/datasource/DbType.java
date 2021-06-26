package com.drop.leaves.datasource;

/**
 * @author mbs on 2021-06-26 13:52
 */
public enum DbType {

    MASTER("master"),

    SLAVE("slave"),
    ;
    private final String code;

    DbType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
