package org.tea.plus.agent.httpservlet;

import javax.servlet.http.HttpServletRequest;

/**
 * @author by mobingsen on 2021/6/20 13:15
 */
public class DispatcherServletCollect {

    public static void begin(Object[] params) {
        // javax.servlet.http.HttpServletRequest
        HttpServletRequest request = (HttpServletRequest) params[0];
        System.out.println("远程地址是: " + request.getRequestURI());
    }
}
