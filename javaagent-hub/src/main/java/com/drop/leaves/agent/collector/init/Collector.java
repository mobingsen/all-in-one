package com.drop.leaves.agent.collector.init;

import javassist.CtClass;

/**
 * 采集接口
 * @author mobingsen
 */
public interface Collector {

	/**
	 * 判断是否为采集目录
	 * 
	 * @param className
	 * @param loader
	 * @param ctclass
	 * @return
	 */
	boolean isTarget(String className, ClassLoader loader, CtClass ctclass);

	/**
	 * 对目标类进行转
	 * @param loader
	 * @param className
	 * @param classfileBuffer
	 * @param ctclass
	 * @return
	 * @throws Exception
	 */
	byte[] transform(ClassLoader loader, String className, byte[] classfileBuffer, CtClass ctclass) throws Exception;
}
