package org.tea.plus.agent.collector.collection;

import org.tea.plus.agent.collector.init.AbstractCollector;
import org.tea.plus.agent.collector.init.AgentLoader;
import org.tea.plus.agent.collector.init.Collector;
import org.tea.plus.agent.collector.init.NotProguard;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;

/**
 * 对 Controller 层进行采集
 *
 * @author mobingsen
 */
@NotProguard
public class SpringControllerCollector extends AbstractCollector implements Collector {

    @NotProguard
    public static SpringControllerCollector INSTANCE = new SpringControllerCollector();
    private static final String beginSrc;
    private static final String endSrc;
    private static final String errorSrc;

    private String rootRequestUrl = "";

    static {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("com.apm.collects.SpringControllerCollects instance= ");
        sbuilder.append("com.apm.collects.SpringControllerCollects.INSTANCE;\r\n");
        sbuilder.append("com.apm.collects.SpringControllerCollects.WebStatistics statistic =(com.apm.collects.SpringControllerCollects.WebStatistics)instance.begin(\"%s\",\"%s\");");
        sbuilder.append("statistic.urlAddress=\"%s\";");
        beginSrc = sbuilder.toString();
//        sbuilder = new StringBuilder();
        sbuilder.setLength(0);
        sbuilder.append("instance.end(statistic);");
        endSrc = sbuilder.toString();
        sbuilder.setLength(0);
//        sbuilder = new StringBuilder();
        // 父类的方法
        sbuilder.append("instance.error(statistic,e);");
        errorSrc = sbuilder.toString();
    }

    /**
     * 判断是否是采集对象
     */
    @Override
    public boolean isTarget(String className, ClassLoader loader, CtClass ctclass) {
        boolean result = false;
        try {
            for (Object obj : ctclass.getAnnotations()) {
                // 通过正则表达示计算出RequestMapping 地址
                if (obj.toString().startsWith("@org.springframework.web.bind.annotation.RequestMapping")) {
                    rootRequestUrl = getAnnotationValue("value", obj.toString());
                } else if (obj.toString().startsWith("@org.springframework.stereotype.Controller")) {
                    result = true;
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println(String.format("bit apm run error targetClassName=%s errorMessage=%s", className, e.getClass().getSimpleName() + ":" + e.getMessage()));
        }
        return result;
    }

    @NotProguard
    @Override
    public Statistics begin(String className, String method) {
        WebStatistics webStat = new WebStatistics(super.begin(className, method));
        webStat.controlName = className;
        webStat.methodName = method;
        webStat.logType = "web";
        return webStat;
    }

    @Override
    public void sendStatistics(Statistics stat) {
        sendStatisticByHttp(stat, "webLog");
    }

    /**
     * 对其转换 （插入监控代码）
     */
    @Override
    public byte[] transform(ClassLoader loader, String className, byte[] classfileBuffer, CtClass ctclass) throws Exception {
        AgentLoader byteLoade = new AgentLoader(className, loader, ctclass);
        CtMethod[] methods = ctclass.getDeclaredMethods();
        for (CtMethod m : methods) {
            String requestUrl;
            // 屏蔽非公共方法
            if (!Modifier.isPublic(m.getModifiers())) {
                continue;
            }
            // 屏蔽静态方法
            if (Modifier.isStatic(m.getModifiers())) {
                continue;
            }
            // 屏蔽本地方法
            if (Modifier.isNative(m.getModifiers())) {
                continue;
            }
            // 必须带上 RequestMapping 注解
            if ((requestUrl = getRequestMappingValue(m)) == null) {
                continue;
            }

            AgentLoader.MethodSrcBuild build = new AgentLoader.MethodSrcBuild();
            build.setBeginSrc(String.format(beginSrc, className, m.getName(), rootRequestUrl + requestUrl));
            build.setEndSrc(endSrc);
            build.setErrorSrc(errorSrc);
            byteLoade.updateMethod(m, build);
        }
        return byteLoade.toByteCode();
    }


    private String getRequestMappingValue(CtMethod m) throws ClassNotFoundException {
        for (Object s : m.getAnnotations()) {
            if (s.toString().startsWith("@org.springframework.web.bind.annotation.RequestMapping")) {
                String val = getAnnotationValue("value", s.toString());
                return val == null ? "/" : val;
            }
        }
        return null;
    }

    @NotProguard
    public static class WebStatistics extends Statistics {
        public String urlAddress; //url 地址
        public String controlName; //服务名称
        public String methodName;// 方法名称

        public WebStatistics(Statistics s) {
            super(s);
        }
    }
}
