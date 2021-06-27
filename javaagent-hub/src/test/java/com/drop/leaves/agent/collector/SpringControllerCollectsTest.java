package com.drop.leaves.agent.collector;

import com.drop.leaves.agent.collector.collection.SpringControllerCollector;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

/**
 * @author by mobingsen on 2021/6/22 21:48
 */
public class SpringControllerCollectsTest {

    @Test
    public void isTarget() throws NotFoundException {
        SpringControllerCollector ins = SpringControllerCollector.INSTANCE;
        String className = "com.drop.leaves.agent.collector.SpringControllerCollectsTest$SpringControllerMock";
        ClassLoader loader = SpringControllerCollector.class.getClassLoader();
        ClassPool pool = new ClassPool();
        pool.insertClassPath(new LoaderClassPath(loader));
        CtClass ctClass = pool.get(className);
        boolean result = ins.isTarget(className, loader, ctClass);
        Assertions.assertTrue(result);
    }

    @Test
    public void transformTest() throws Exception {
        SpringControllerCollector ins = SpringControllerCollector.INSTANCE;
        String className = "com.drop.leaves.agent.collector.SpringControllerCollectsTest$SpringControllerMock";
        ClassLoader loader = SpringControllerCollector.class.getClassLoader();
        ClassPool pool = new ClassPool();
        pool.insertClassPath(new LoaderClassPath(loader));
        CtClass ctClass = pool.get(className);
        byte[] classfileBuffer = null;
        if (ins.isTarget(className, loader, ctClass)) {
            ins.transform(loader, className, classfileBuffer, ctClass);
            Class<?> cla = ctClass.toClass();
            SpringControllerMock mock = new SpringControllerMock();
            mock.sayHello();
            System.out.println(mock.getName("", ""));
        } else {
            Assertions.assertTrue(false);
        }
        Path path = new File(System.getProperty("user.dir") + "/target/test-classes/" + ctClass.getSimpleName() + ".class").toPath();
        Files.write(path, ctClass.toBytecode());
        // 留出时间用于上传线程执行
        TimeUnit.MILLISECONDS.sleep(2000);
    }

    @Test
    public void errorTest() throws Exception {
        SpringControllerCollector ins = SpringControllerCollector.INSTANCE;
        String className = "com.drop.leaves.agent.collector.SpringControllerCollectsTest$SpringControllerMock";
        ClassLoader loader = SpringControllerCollector.class.getClassLoader();
        ClassPool pool = new ClassPool();
        pool.insertClassPath(new LoaderClassPath(loader));
        CtClass ctClass = pool.get(className);
        byte[] classfileBuffer = null;
        if (ins.isTarget(className, loader, ctClass)) {
            ins.transform(loader, className, classfileBuffer, ctClass);
            Class<?> cla = ctClass.toClass();
            SpringControllerMock mock = new SpringControllerMock();
            // mock.sayHello();
            try {
                System.out.println(mock.getName("李大钊", "男"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Assertions.assertTrue(false);
        }
        Path path = new File(System.getProperty("user.dir") + "/target/test-classes/" + ctClass.getSimpleName() + ".class").toPath();
        Files.write(path, ctClass.toBytecode());
        // 留出时间用于上传线程执行
        TimeUnit.MILLISECONDS.sleep(2000);
    }

    @Controller
    @RequestMapping("/mock")
    public static class SpringControllerMock {

        @RequestMapping("/hello")
        public void sayHello() {
            System.out.println("sayHello ");
        }

        @RequestMapping(value = "/getName.do", params = {"method=ddddd"})
        @ResponseBody
        public String getName(String name, String sex) {
            if ("男".equals(sex)) {
                throw new RuntimeException("XXXX");
            }
            return "hanmeme";
        }
    }
}
