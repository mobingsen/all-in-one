package com.drop.leaves.mybatisdynamicsqlhub.module.person.mapper;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

/**
 * @author mobingsen
 */
public class YesNoTypeHandler implements TypeHandler<Boolean> {

    @Override
    public void setParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter ? "Yes" : "No");
    }

    @Override
    public Boolean getResult(ResultSet rs, String columnName) throws SQLException {
        return "Yes".equals(rs.getString(columnName));
    }

    @Override
    public Boolean getResult(ResultSet rs, int columnIndex) throws SQLException {
        return "Yes".equals(rs.getString(columnIndex));
    }

    @Override
    public Boolean getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return "Yes".equals(cs.getString(columnIndex));
    }
}