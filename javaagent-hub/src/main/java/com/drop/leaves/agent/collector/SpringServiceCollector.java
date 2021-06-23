package com.drop.leaves.agent.collector;

import javassist.CtClass;
import javassist.CtMethod;

import java.lang.reflect.Modifier;

/**
 * @author by mobingsen on 2021/6/20 22:47
 */
public class SpringServiceCollector implements Collect {

    private static String beginSrc;
    private static String endSrc;
    private static String errorSrc;

    static {
        StringBuilder builder = new StringBuilder();
        builder.append("com.drop.leaves.agent.collector.SpringServiceCollector.INSTANCE.begin(\"%s\", \"%s\")");
    }

    public static final SpringServiceCollector INSTANCE = new SpringServiceCollector();

    @Override
    public boolean isTarget(String className, ClassLoader loader, CtClass ctclass) {
        try {
            for (Object o : ctclass.getAnnotations()) {
                if (o.toString().startsWith("@org.springframework.stereotype.Service")) {
                    return true;
                }
            }
        } catch (ClassNotFoundException e) {
            // e.printStackTrace();
            System.err.println(e.getMessage());
        }
        return false;
    }

    private long begin;



    @Override
    public byte[] transform(ClassLoader loader, String className, byte[] classfileBuffer, CtClass ctclass) throws Exception {
        for (CtMethod method : ctclass.getMethods()) {
            AgentLoader agentLoader = new AgentLoader(className, loader, ctclass);
            if (!Modifier.isPublic(method.getModifiers())) {
                continue;
            }
            if (Modifier.isNative(method.getModifiers())) {
                continue;
            }
            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }
            AgentLoader.MethodSrcBuild srcBuild = new AgentLoader.MethodSrcBuild();
            srcBuild.setBeginSrc(beginSrc);
            srcBuild.setEndSrc(endSrc);
            srcBuild.setErrorSrc(errorSrc);
            agentLoader.updateMethod(method, srcBuild);
        }
        return new byte[0];
    }
}
