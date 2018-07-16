package com.foriseland.fif.telnet.command;

import com.alibaba.fastjson.JSON;
import com.foriseland.fif.ServiceContext;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class InvokeHandler implements CommandHandler {

    private final String command;

    public InvokeHandler(final String command){
        this.command = command;
    }

    @Override
    public String handle() {
        BeanFactory beanFactory = ServiceContext.getBeanFactory();
        String beanNameOrType = command.substring(command.indexOf(" ") + 1,
                                                  command.substring(0, command.indexOf("(")).lastIndexOf("."));
        String methodName = command.substring(command.substring(0, command.indexOf("(")).lastIndexOf(".") + 1,
                                              command.indexOf("("));
        Object obj = beanFactory.getBean(beanNameOrType);
        StringBuilder builder = new StringBuilder();
        Method[] methods = obj.getClass().getDeclaredMethods();
        String[] paraStr = command.substring(command.indexOf("(") + 1, command.lastIndexOf(")")).split(";");
        for (Method method : methods) {
            if (!methodName.equals(method.getName()) || method.getParameterTypes().length != paraStr.length) {
                continue;
            }
            // builder.append(method.getReturnType().getName() + " "
            // + obj.getClass().getName() + "." + method.getName());
            Class<?>[] cls = method.getParameterTypes();
            Object[] para = new Object[cls.length];
            for (int i = 0; i < cls.length; i++) {
                if (cls[i].isPrimitive()) {
                    if (paraStr[i].indexOf("\"") > 0) {
                        para[i] = paraStr[i].substring(1, paraStr[i].length() - 1);
                    } else if (paraStr[i].equals("true") || paraStr[i].equals("false")) {
                        para[i] = Boolean.getBoolean(paraStr[i]);
                    } else {
                        if (cls[i].isAssignableFrom(Double.class)) {
                            para[i] = Double.valueOf(paraStr[i]);
                        } else if (cls[i].isAssignableFrom(int.class)) {
                            para[i] = Integer.valueOf(paraStr[i]);
                        } else if (cls[i].isAssignableFrom(long.class)) {
                            para[i] = Long.valueOf(paraStr[i]);
                        } else if (cls[i].isAssignableFrom(char.class)) {
                            para[i] = Character.valueOf(paraStr[i].charAt(0));
                        } else if (cls[i].isAssignableFrom(short.class)) {
                            para[i] = Short.valueOf(paraStr[i]);
                        } else if (cls[i].isAssignableFrom(float.class)) {
                            para[i] = Float.valueOf(paraStr[i]);
                        }
                    }
                } else if (cls[i].isEnum()) {
                    para[i] = JSON.parseObject(paraStr[i], cls[i]);
                } else if (cls[i].isArray()) {
                    para[i] = JSON.parseArray(paraStr[i], cls[i]);
                } else if (cls[i].isAssignableFrom(List.class)) {
                    Type[] types = method.getGenericParameterTypes();
                    Type fc = types[i];
                    if (fc instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) fc;
                        Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0]; // ��4�� �õ��������class���Ͷ���
                        para[i] = JSON.parseArray(paraStr[i], genericClazz);
                    }else{
                        para[i] = JSON.parseArray(paraStr[i]);
                    }

                } else {
                    para[i] = JSON.parseObject(paraStr[i], cls[i]);
                }
            }
            long start = System.currentTimeMillis();
            try {
                Object ret = method.invoke(obj, para);
                builder.append("result:" + ret.getClass().getName() + " " + JSON.toJSON(ret));
            } catch (Exception e) {
                builder.append(e);
            }
            long end = System.currentTimeMillis();
            builder.append("\r\nrun time: " + (end - start) + "ms\n");
            return builder.toString();
        }
        return "there is no such method!";
    }

}
