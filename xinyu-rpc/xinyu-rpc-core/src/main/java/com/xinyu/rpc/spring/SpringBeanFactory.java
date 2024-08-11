
package com.xinyu.rpc.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Map;

@Component("springBeanFactory")
public class SpringBeanFactory implements ApplicationContextAware {

    /**
     * ioc容器,由springboot自动配置的
     */
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }


    /**
     * 根据Class获取bean
     * @param cls
     * @param <T>
     * @return
     */
    public static   <T> T getBean(Class<T> cls) {
        return context.getBean(cls);
    }

    /**
     * 根据beanName获取bean
     * @param beanName
     * @return
     */
    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    /***
     * 获取有指定注解的对象
     * @param annotationClass
     * @return
     */
    public static Map<String, Object> getBeanListByAnnotationClass(Class<? extends Annotation> annotationClass) {
         return context.getBeansWithAnnotation(annotationClass);
    }

    /**
     * 向容器注册单例bean
     * @param bean
     */
    public static void registerSingleton(Object bean) {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
        // 让bean完成Spring初始化过程中所有增强器检验，只是不重新创建bean
        beanFactory.applyBeanPostProcessorsAfterInitialization(bean,bean.getClass().getName());
        //将bean以单例的形式入驻到容器中，此时通过bean.getClass().getName()或bean.getClass()都可以拿到放入Spring容器的Bean
        beanFactory.registerSingleton(bean.getClass().getName(),bean);
    }
}
