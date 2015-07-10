package com.xafero.dynkt.util;

import java.lang.reflect.Field;
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

	public static Method findMethod(Class<?> clazz, String methodName) {
		for (Method method : clazz.getDeclaredMethods())
			if (method.getName().equalsIgnoreCase(methodName))
				return method;
		return null;
	}

	public static Method findMethod(Class<?> clazz, String method, Class<?>... prmTypes) {
		try {
			return clazz.getMethod(method, prmTypes);
		} catch (Exception e) {
			return null;
		}
	}

	public static void set(Object obj, Class<?> clazz, String fieldName, Object value) {
		try {
			final Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(obj, value);
		} catch (Throwable e) {
			throw new RuntimeException("set", e);
		}
	}
}