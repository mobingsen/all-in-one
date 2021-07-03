package com.drop.leaves.agent;

import javassist.*;

/**
 * @author by mobingsen on 2021/6/19 13:13
 */
public class JavassistMain {

    public static void main(String[] args) throws NotFoundException, CannotCompileException, InstantiationException, IllegalAccessException {
        ClassPool pool = new ClassPool(true);
        // insertClassPath是指查到首位，javassist.ClassPool.appendClassPath(javassist.ClassPath)放到尾部
        pool.insertClassPath(new LoaderClassPath(JavassistMain.class.getClassLoader()));
        CtClass targetClass = pool.makeClass("com.drop.leaves.agent.JavassistMain.HelloImpl");
        targetClass.addInterface(pool.get(IHello.class.getName()));

        CtClass[] parameters = new CtClass[]{pool.get(String.class.getName())};
        CtMethod method = new CtMethod(pool.get(void.class.getName()), "hello", parameters, targetClass);

        String src = "{" +
                "System.out.println($1);" +
                "int i = System.nanoTime();" +
                "int k = \"abc\"" +
                "System.out.println($args.toString());" +
//                "System.out.println($r.toString());" +
                "System.out.println($type.toString());" +
                "System.out.println($class.toString());" +
                "System.out.println(\"hello \" + $1);" +
                "}";
        method.setBody(src);
        targetClass.addMethod(method);

        IHello instance = (IHello) targetClass.toClass().newInstance();
        instance.hello("world");
    }

    public interface IHello {
        void hello(String name);
    }
}
