package com.foriseland.fjf.thread;

/**
 * @className: CommonThreadLocal
 * @describe: 线程携带的通用对象
 * @author: wangHiayang
 * @createTime 2017年2月20日 下午8:23:52
 */
public class ThreadLocalContext {

   private static ThreadLocal<Object> threadLocal = new ThreadLocal<Object>();


    public static void setContextBean(Object object){
        threadLocal.set(object);
    }

    public static Object getContextBean(){
        return threadLocal.get();
    }

}
