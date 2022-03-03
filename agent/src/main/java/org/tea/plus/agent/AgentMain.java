package org.tea.plus.agent;

import java.lang.instrument.Instrumentation;

/**
 * javaagent 是java1.5之后引入的特性，其主要作用是在class被加载之前对其拦截，以插入我们的监听字节码。
 * @author by mobingsen on 2021/6/19 9:47
 */
public class AgentMain {

    public static void main(String[] args) {
        System.out.println("agent main");
    }

    public static void premain(String args, Instrumentation inst) {
        System.out.println("agent premain");
    }
}
