package com.drop.leaves.mybatis;

import java.sql.*;

/**
 * @author by mobingsen on 2021/6/5 14:20
 */
public class JdbcDemo {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection("");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("select * from user")) {
            String string = resultSet.getString(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
