package com.foriseland.fif.mq.processor;

import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.foriseland.fif.mq.annotation.FifMqConsumer;
import com.foriseland.fif.mq.config.FifMqConfig;
import com.foriseland.fif.mq.constant.Constant;
import com.foriseland.fif.mq.consumer.RocketMQConsumer;
import com.foriseland.fif.mq.util.CaseStringUtil;
import com.xxl.conf.core.XxlConfClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Component;


import java.util.Set;

import static com.foriseland.fif.mq.util.CaseStringUtil.*;

/**
 * <p>ClassName: RocketMqRegistryPostProcessor</p>
 * <p>Description: 注入ioc 扫描注解反射实例化注入</p>
 * <p>Company: 梦想工厂--基础架构部/p>
 *
 * @author weizhuang
 * @date 2018-5-28
 */
@Component
public class RocketMqRegistryPostProcessor implements BeanPostProcessor, ApplicationContextAware,BeanDefinitionRegistryPostProcessor {

    private Logger logger = Logger.getLogger(RocketMqRegistryPostProcessor.class);

    private static ApplicationContext applicationContext = null;

    private static FifMqConfig fifMqConfig;


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

        //获取ioc config实例属性
        fifMqConfig = (FifMqConfig)applicationContext.getBean(Constant.FIFROCKETCONFIGNAME);
        fifMqConfig.setNameServer(XxlConfClient.get(fifMqConfig.getNameServer(),""));
        if(fifMqConfig == null){
            logger.info("rocketMqConfig is null,初始化配置异常,rocketMq config 是否初始化配置!");
            throw new RuntimeException("rocketMqConfig is null,初始化配置异常,rocketMq config 是否初始化配置!");
        }
        if(!StringUtils.isBlank(fifMqConfig.getBasePackage())){
            //是否使用默认的filter，使用默认的filter意味着只扫描那些类上拥有Component、Service、Repository或Controller注解的类。
            ClassPathScanningCandidateComponentProvider beanScanner = new ClassPathScanningCandidateComponentProvider(true);
            //filter增加过滤条件 过滤自定义注解
            TypeFilter consumerFilter = new AnnotationTypeFilter(FifMqConsumer.class);
            //添加过滤
            beanScanner.addIncludeFilter(consumerFilter);
            //扫描包路径下 所有符合条件的bean
            Set<BeanDefinition> beanDefinitions = beanScanner.findCandidateComponents(fifMqConfig.getBasePackage());
            for (BeanDefinition bean : beanDefinitions) {
                //beanName通常由对应的BeanNameGenerator来生成，比如Spring自带的AnnotationBeanNameGenerator、DefaultBeanNameGenerator等，也可以自己实现。
                String beanName = bean.getBeanClassName();
                MessageListenerConcurrently contextBean = (MessageListenerConcurrently)getBean(beanName);
                //初始化beanName  规则:类名首字母小写
                beanName = CaseStringUtil.toSubStringBeanName(beanName);
                //通过bean路径实例化 监听者
                //MessageListenerConcurrently contextBean = (MessageListenerConcurrently)applicationContext.getBean(beanName);
                if(contextBean == null){
                    logger.info("rocketMq消费端 没有被springioc容器 实例化");
                    throw new RuntimeException("rocketMqConfig is null,初始化配置异常,rocketMq config 是否初始化配置!");
                }
                //获取bean  class
                Class clazz = contextBean.getClass();
                //getFifService(contextBean,clazz,autowireCapableBeanFactory);
                //注入生产者(因为在实例化消费端的时候 已经通过ioc 容器取到回调监听 所以无法通过 BeanPostProcessor 注入 BeanPostProcessor是在ioc容器实例化获取bean前执行)
                getFifMqProducer(contextBean, clazz, fifMqConfig);
                //获取class 注解
                FifMqConsumer consumer = (FifMqConsumer) clazz.getAnnotation(FifMqConsumer.class);
                //沒有配置zk 地址 拋出异常
                if(StringUtils.isBlank(fifMqConfig.getNameServer()) && StringUtils.isBlank(consumer.nameServer())){
                    logger.info("rocketMq zk配置 null,初始化配置异常,rocketMq config 或者 FifMqConsumer是否初始化配置!");
                    throw new RuntimeException("rocketMq zk配置 null,初始化配置异常,rocketMq config 或者 FifMqConsumer是否初始化配置!");
                }
                //新建消费者  注入监听者 和注解配置属性 没有配置 放入默认值
                RootBeanDefinition beanDefinition = new RootBeanDefinition();
                beanDefinition.setBeanClass(RocketMQConsumer.class);
                //beanDefinition.getPropertyValues().addPropertyValue("listener",contextBean);
                //两个地方都配置zk地址 有限读取配置文件类的
                if(!StringUtils.isBlank(fifMqConfig.getNameServer())){
                    beanDefinition.getPropertyValues().addPropertyValue("nameServer",fifMqConfig.getNameServer());
                }else{
                    beanDefinition.getPropertyValues().addPropertyValue("nameServer",consumer.nameServer());
                }
                beanDefinition.getPropertyValues().addPropertyValue("groupName",consumer.groupName());
                beanDefinition.getPropertyValues().addPropertyValue("topic",consumer.topic());
                beanDefinition.getPropertyValues().addPropertyValue("tags",consumer.tags());
                beanDefinition.getPropertyValues().addPropertyValue("formWhere",consumer.formWhere());
                beanDefinition.getPropertyValues().addPropertyValue("messageModel",consumer.messageModel());
                //初始化 调用init方法进行启动消费者
                beanDefinition.setInitMethodName("init");
                //注入ioc
                beanDefinitionRegistry.registerBeanDefinition(beanName+Constant.FIFROCKETCONSUMERNAMEAFFIX, beanDefinition);
            }
        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        getFifMqListener(bean,bean.getClass(),applicationContext,beanName);
        getFifMqProducer(bean, bean.getClass(), fifMqConfig);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
