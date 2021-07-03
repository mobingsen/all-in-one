package com.drop.leaves.agent;

import javassist.*;

/**
 * @author by mobingsen on 2021/6/20 10:14
 */
public class UtilMonitor {

    public static void main(String[] args) throws NotFoundException, CannotCompileException {
        ClassPool pool = new ClassPool(true);
        String targetClassName = "com.drop.leaves.agent.BitStringUtil";
        CtClass targetClass = pool.get(targetClassName);
        CtMethod method = targetClass.getDeclaredMethod("addString");

        CtMethod agentMethod = CtNewMethod.copy(method, method.getName() + "$agent", targetClass, null);
        targetClass.addMethod(agentMethod);
        String src = "{" +
                "long begin = System.nanoTime();" +
                "Object result =" + method.getName() + "$agent($$);" +
                "long end = System.nanoTime();" +
                "System.out.println(end - begin);" +
                "return ($r) result;" +
                "}";
        method.setBody(src);
        targetClass.toClass();
        BitStringUtil util = new BitStringUtil();
        util.addString(1000);
    }
}
