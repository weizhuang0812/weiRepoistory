package com.foriseland.fif.mq.util;

import com.foriseland.fif.mq.annotation.FifMqProducer;
import com.foriseland.fif.mq.config.FifMqConfig;
import com.foriseland.fif.mq.consumer.RocketMQConsumer;
import com.foriseland.fif.mq.producer.RocketMQProducer;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import static com.foriseland.fif.mq.constant.Constant.FIFROCKETCONSUMERLISTENERNAME;
import static com.foriseland.fif.mq.constant.Constant.FIFROCKETCONSUMERNAMEAFFIX;

/**
 * <p>ClassName: CaseStringUtil</p>
 * <p>Description: 字符串操作类</p>
 * <p>Company: 梦想工厂--基础架构部/p>
 *
 * @author weizhuang
 * @date 2018-05-28
 */
public class CaseStringUtil {


    private static Logger logger = Logger.getLogger(CaseStringUtil.class);

    //获取beanName
    public static String toSubStringBeanName(String beanName){
        int one = beanName.lastIndexOf(".");
        if(one!= -1)
            beanName = beanName.substring((one+1),beanName.length());
        else
            return null ;

        return toLowerCaseFirstOne(beanName);
    }

    //首字母转小写
    public static String toLowerCaseFirstOne(String s){
        if(Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }


    //首字母转大写
    public static String toUpperCaseFirstOne(String s){
        if(Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    //首字母转大写
    public static String getRocketConsumerListenerName(String s){

        if(s.indexOf(FIFROCKETCONSUMERNAMEAFFIX)!= -1){
            String result = s.replaceAll(FIFROCKETCONSUMERNAMEAFFIX, "");
            return result;
        }else{
            return  null ;
        }
    }

    /**
     * 注入fif生产者
     * @param bean 注入bean
     * @param clazz 注入class
     * @param fifMqConfig 默认配置
     * @return obj
     */
    public static Object getFifMqProducer(Object bean , Class clazz , FifMqConfig fifMqConfig){

        if (clazz.isAnnotationPresent(FifMqProducer.class)) {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields
                    ) {
                //获取属性使用生产端注解的
                boolean isAnon = field.isAnnotationPresent(FifMqProducer.class);
                if(isAnon) {
                    //获取注解属性
                    field.setAccessible(true);
                    FifMqProducer producer = (FifMqProducer) field.getAnnotation(FifMqProducer.class);
                    try {
                        //实例化生产端
                        RocketMQProducer rocketMQProducer = new RocketMQProducer();
                        if(StringUtils.isBlank(producer.nameServer())){
                            rocketMQProducer.setNameServer(fifMqConfig.getNameServer());
                        }else{
                            rocketMQProducer.setNameServer(producer.nameServer());
                        }
                        rocketMQProducer.setGroupName(producer.groupName());
                        rocketMQProducer.init();
                        field.set(bean,rocketMQProducer);
                    }catch (IllegalAccessException e){
                        logger.error(e.getStackTrace());
                        e.printStackTrace();
                        throw new RuntimeException("请检查生产者注入方式~！ 参数不能为null ");
                    }
                }
            }
        }
        return bean;
    }
    public static Object getFifMqListener(Object bean , Class clazz , ApplicationContext applicationContext,String beanName){

        if (bean instanceof RocketMQConsumer) {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields
                    ) {
                try {
                    if(field.getName().equals(FIFROCKETCONSUMERLISTENERNAME)){
                        field.setAccessible(true);
                        String rocketConsumerListenerName = getRocketConsumerListenerName(beanName);
                        if(rocketConsumerListenerName == null){
                            throw new RuntimeException("请检查消费监听命名方式！");
                        }
                        Object rocketMQListener1 = applicationContext.getBean(rocketConsumerListenerName);
                        field.set(bean,rocketMQListener1);
                    }
                }catch (IllegalAccessException e){
                    logger.error(e.getStackTrace());
                    e.printStackTrace();
                    throw new RuntimeException("请检查生产者注入方式~！ 参数不能为null ");
                }
            }
        }
        return bean;
    }


    /**
     * 注入fif service
     * @param bean 注入bean
     * @param clazz 注入class
     * @return obj
     */
    public static Object getFifService(Object bean , Class clazz, AutowireCapableBeanFactory applicationContext){
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields
                    ) {
                //获取属性使用生产端注解的
                boolean isAnon = field.isAnnotationPresent(Autowired.class);
                boolean isAnon1 = field.isAnnotationPresent(Resource.class);
                if (isAnon || isAnon1) {
                    //获取注解属性
                    field.setAccessible(true);
                    try {
                        System.out.println(field.getName());
                        Object bean1 = applicationContext.getBean(field.getName());
                        field.set(bean, bean1);
                    } catch (IllegalAccessException e) {
                        logger.error(e.getStackTrace());
                        throw new RuntimeException("请检查生产者注入方式~！ 参数不能为null ");
                    }
                }
            }
        return bean;
    }
    /**
     * 跳过spring实例化 通过className 手动实例化bean
     * @param className
     * @return
     */
    public static Object getBean(String className) {
        Class cls = null;
        try {
            cls = Class.forName(className);//对应Spring ->bean -->class
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("初始化监听类错误");
        }
        Constructor[] cons = null;//得到所有构造器
        try {
            cons = cls.getConstructors();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("初始化监听类构造器错误！");
        }
        if (cons == null || cons.length < 1) {
            throw new RuntimeException("初始化监听类没有默认构造方法！");
        }
        //如果上面没错，就有构造方法
        try {
            Constructor defCon = cons[0];//得到默认构造器,第0个是默认构造器，无参构造方法
            Object obj = defCon.newInstance();//实例化，得到一个对象 //Spring - bean -id
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
