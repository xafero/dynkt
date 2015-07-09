package com.xafero.dynkt.util;

import java.lang.reflect.Method;

public class ReflUtils {

	private ReflUtils() {
	}

	public static Object invoke(Class<?> clazz, Object obj, String methodName, Object... args) {
		try {
			Method method = findMethod(clazz, methodName);
			method.setAccessible(true);
			Object result = method.invoke(obj, args);
			return result;
		} catch (Throwable e) {
			throw new RuntimeException("invoke", e);
		}
	}

	private static Method findMethod(Class<?> clazz, String methodName) {
		for (Method method : clazz.getDeclaredMethods())
			if (method.getName().equalsIgnoreCase(methodName))
				return method;
		return null;
	}
}