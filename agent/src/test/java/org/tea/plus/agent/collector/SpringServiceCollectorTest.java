package org.tea.plus.agent.collector;

import org.tea.plus.agent.collector.collection.SpringServiceCollector;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

class SpringServiceCollectorTest {

    @Test
    void isTarget() throws NotFoundException {
        String className = "org.tea.plus.agent.collector.SpringServiceCollectorTest$StringService";
        ClassLoader classLoader = SpringServiceCollectorTest.class.getClassLoader();
        ClassPool pool = new ClassPool(true);
        CtClass ctClass = pool.get(className);
        boolean target = org.tea.plus.agent.collector.SpringServiceCollector.INSTANCE.isTarget(className, classLoader, ctClass);
        Assertions.assertTrue(target);
    }

    @Test
    void transform() throws Exception {
        SpringServiceCollector ins = SpringServiceCollector.INSTANCE;
        ClassLoader loader = SpringServiceCollector.class.getClassLoader();
        String className = "org.tea.plus.agent.collector.SpringServiceCollectorTest$StringServiceMock";
        byte[] classfileBuffer = null;
        ClassPool pool = new ClassPool();
        pool.insertClassPath(new LoaderClassPath(loader));
        CtClass cclass = pool.get(className);
        ins.transform(loader, className, classfileBuffer, cclass);
        Class<?> cla = cclass.toClass();
        StringServiceMock mock = new StringServiceMock();
        mock.sayHello("hanmeme");
        TimeUnit.MILLISECONDS.sleep(2000);
    }

    // @Service
    @Service(value = "StringServiceMockBean")
    public static class StringServiceMock {

        public void sayHello(String name) {
            System.out.println("hello " + name);
        }
    }
}