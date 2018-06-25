package com.foriseland.fjf.sequence.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import com.foriseland.fjf.exception.BaseExcepton;
import com.foriseland.fjf.sequence.annotation.SequenceField;
import com.foriseland.fjf.sequence.handle.SequenceUtil;

/**
 * @author Administrator 反射工具
 */
public class ReflectHelper {
	/**
	 * 获取obj对象fieldName的Field
	 * 
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static Field getFieldByFieldName(Object obj, String fieldName) {
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
			}
		}
		return null;
	}

	/**
	 * 检查是否含有分页或本来就是分页类
	 * 
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object isPage(Object obj, String fieldName) {

		if (obj instanceof java.util.Map) {
			Map<String, Object> map = (Map<String, Object>) obj;
			return map.get(fieldName);

		} else {
			for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass
					.getSuperclass()) {
				try {
					return superClass.getDeclaredField(fieldName);
				} catch (NoSuchFieldException e) {
				}
			}
			return null;
		}

	}

	/**
	 * 获取obj对象fieldName的属性值
	 * 
	 * @param obj
	 * @param fieldName
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Object getValueByFieldName(Object obj, String fieldName)
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field field = getFieldByFieldName(obj, fieldName);
		Object value = null;
		if (field != null) {
			if (field.isAccessible()) {
				value = field.get(obj);
			} else {
				field.setAccessible(true);
				value = field.get(obj);
				field.setAccessible(false);
			}
		}
		return value;
	}

	/**
	 * 设置obj对象fieldName的属性值
	 * 
	 * @param obj
	 * @param fieldName
	 * @param value
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	public static void setValueByFieldName(Object obj, String fieldName, Object value)
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {

		if (obj instanceof java.util.Map) {
			Map<String, Object> map = (Map<String, Object>) obj;
			map.put(fieldName, value);
		} else {
			Field field = obj.getClass().getDeclaredField(fieldName);
			if (field.isAccessible()) {
				field.set(obj, value);
			} else {
				field.setAccessible(true);
				field.set(obj, value);
				field.setAccessible(false);
			}

		}
	}

	private static Object getFieldValueByName(String fieldName, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(o, new Object[] {});
			return value;
		} catch (Exception e) {
			return null;
		}
	}

	public static Object getFiledValues(Field field,Object obj) {
		return getFieldValueByName(field.getName(),obj);
	}

	public static void setSeqValue(Object obj) throws SecurityException, IllegalArgumentException, NoSuchFieldException,
			IllegalAccessException, BaseExcepton {
		Field[] fileds = obj.getClass().getDeclaredFields();
		SequenceUtil sequenceUtil = SequenceUtil.create();
		for (Field field : fileds) {
			SequenceField sf = field.getAnnotation(SequenceField.class);
			if (null != sf) {
				long id = sequenceUtil.sequenceNextVal(obj.getClass());
				Object value = getFiledValues(field,obj); // 获得Id -> Value
				if(null == value){
					ReflectHelper.setValueByFieldName(obj, field.getName(), id);
				}
				return;
			}

		}

	}
}
