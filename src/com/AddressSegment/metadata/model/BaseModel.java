package com.AddressSegment.metadata.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseModel {
	public static boolean inExceptMethods(String methodName, String[] exceptMethods){
		for(String exceptMethod : exceptMethods){
			if(exceptMethod.equals(methodName)){
				return true;
			}
		}
		return false;
	}
	
	public static List<Method> findGetterMethods(Class<?> _class, String... exceptMethods){
		Method[] allMethods = _class.getDeclaredMethods();
		List<Method> getters = new ArrayList<Method>();
		for(Method method : allMethods){
			if(method.getName().startsWith("get") && !inExceptMethods(method.getName(), exceptMethods)){
				getters.add(method);
			}
		}
		return getters;
	}

//	@Override
	public String toString() {
		StringBuffer strObj = new StringBuffer(getClass().getName());
		strObj.append(" -> [");
		try {
			appendObjectAttributes(strObj);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strObj.append("];").toString();
		
	}

	private void appendObjectAttributes(StringBuffer strObj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		// TODO Auto-generated method stub
		List<Method> getters = findGetterMethods(getClass(), "getClass");
		Method getter;
		if (getters.size() > 0) {
			getter = getters.get(0);
			strObj.append(getter.getName()).append(": ").append(getter.invoke(this));
		}
		for (int i = 1; i < getters.size(); ++i){
			getter = getters.get(i);
			strObj.append(", ").append(getter.getName()).append(": ").append(getter.invoke(this));
		}
	}
	
	

}
