package com.drop.leaves.agent.httpservlet;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * @author by mobingsen on 2021/6/20 13:19
 */
public class AgentMain implements ClassFileTransformer {

    /**
     * 在应用启动前调用
     * @param agentArgs
     * @param inst
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new AgentMain());
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer)
            throws IllegalClassFormatException {
        String target = "javax.servlet.http.HttpServlet";
        if (target.equals(className)) {
            try {
                return buildClass(target, loader);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private byte[] buildClass(String target, ClassLoader loader) throws NotFoundException, CannotCompileException, IOException {
        ClassPool pool = new ClassPool();
        pool.insertClassPath(new LoaderClassPath(loader));
        CtClass cla = pool.get(target);
        CtClass[] params = {
                pool.get("javax.servlet.http.HttpServletRequest"),
                pool.get("javax.servlet.http.HttpServletResponse")
        };
        CtMethod method = cla.getDeclaredMethod("service", params);
        method.insertBefore("com.drop.leaves.agent.httpservlet.DispatcherServletCollect.begin($args, $args)");
        pool.get("com.drop.leaves.agent.httpservlet.DispatcherServletCollect").toClass(loader);
        return cla.toBytecode();
    }
}
