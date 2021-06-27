package com.drop.leaves.agent.collector;

import com.drop.leaves.agent.collector.collection.JdbcCommonCollector;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.*;

/**
 * @author by mobingsen on 2021/6/23 20:49
 */
public class JdbcCommonCollectsTest {

    @Test
    public void isTarget() throws NotFoundException {
        JdbcCommonCollector ins = JdbcCommonCollector.INSTANCE;
        String className = "com.mysql.cj.jdbc.NonRegisteringDriver";
        ClassLoader loader = JdbcCommonCollector.class.getClassLoader();
        ClassPool pool = new ClassPool();
        pool.insertClassPath(new LoaderClassPath(loader));
        CtClass ctClass = pool.get(className);
        Assertions.assertTrue(ins.isTarget(className, loader, ctClass));
    }

    @Test
    public void proxyConnectionTest() throws SQLException {
        JdbcCommonCollector ins = JdbcCommonCollector.INSTANCE;
        Connection connection = ins.proxyConnection(getConnection());
        PreparedStatement preparedStatement = connection.prepareStatement("select * from user");
        preparedStatement.execute();
    }

    @Test
    public void transform() throws NotFoundException {
        JdbcCommonCollector instance = JdbcCommonCollector.INSTANCE;
        String className = "com.mysql.cj.jdbc.NonRegisteringDriver";
        ClassLoader loader = JdbcCommonCollector.class.getClassLoader();
        ClassPool pool = new ClassPool();
        pool.insertClassPath(new LoaderClassPath(loader));
        CtClass ctClass = pool.get(className);

    }

    private Connection getConnection() {
        return null;
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql?useUnicode=true", "root", "123456");
        PreparedStatement ps = conn.prepareStatement("select * from user");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
    }
}
