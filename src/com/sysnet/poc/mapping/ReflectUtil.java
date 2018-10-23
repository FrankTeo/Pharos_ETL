package com.sysnet.poc.mapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;

import com.sysnet.poc.util.OdsLogger;

/**
 * 反射工具类
 * 
 * @author
 * 
 */
public class ReflectUtil {
	private static final Log log = OdsLogger.getLog("insureLog4j", "Insure");

	/**
	 * 通过反射得到对象
	 * 
	 * @param clazz
	 * @param methodNm
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object getObjByReflect(Object clazz, String methodNm) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		if (methodNm == null) {
			log.error("Method name " + methodNm + " is null!");
			return null;
		}
		if (clazz == null)
			return null;
		Class<?> myClass = clazz.getClass();
		Method method = myClass.getMethod(methodNm);
		if (method == null) {
			log.error("Method " + methodNm + " is null!");
			return null;
		}
		return method.invoke(clazz);
	}

	/**
	 * 目前只只支持<=2个参数的反射调用，如果需要不定数量的参数，请自己重载
	 * 
	 * @param clazz
	 * @param methodNm
	 * @param parameters
	 * @param args1
	 * @param args2
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */

	@SuppressWarnings("rawtypes")
	public static Object getObjByReflectByParameters(Object clazz, String methodNm, Class[] parameterTypes, Object args1, Object args2) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		if (methodNm == null) {
			log.error("Method name " + methodNm + " is null!");
			return null;
		}
		if (clazz == null)
			return null;

		Class<?> myClass = clazz.getClass();
		Method method = myClass.getDeclaredMethod(methodNm, parameterTypes);
		if (method == null) {
			log.error("Method " + methodNm + " is null!");
			return null;
		}
		return method.invoke(clazz, args1, args2);
	}

}
