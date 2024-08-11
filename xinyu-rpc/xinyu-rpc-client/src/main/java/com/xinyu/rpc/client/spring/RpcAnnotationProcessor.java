package com.xinyu.rpc.client.spring;

import com.xinyu.rpc.annotation.XYrpcRemote;

import com.xinyu.rpc.proxy.ProxyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@Component
@Slf4j
public class RpcAnnotationProcessor implements BeanPostProcessor, ApplicationContextAware {

    private ProxyFactory proxyFactory;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 获取bean的field,判断是否有自定义的注解
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {

                XYrpcRemote hrpcRemote = field.getAnnotation(XYrpcRemote.class);
                if (hrpcRemote!=null) {
                    // 对该field处理
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    // 拿到filed的类型
                    Class<?> fieldType = field.getType();
                    Object proxy = proxyFactory.newProxyInstance(fieldType);
                    if (proxy!=null) {
                        //将代理注入到filed
                        try {
                            field.set(bean,proxy);
                        } catch (IllegalAccessException e) {
                            log.error("{}的field={}，{}代理注入失败",bean,field,proxy);
                        }
                    }
                }

        }
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.proxyFactory = applicationContext.getBean(ProxyFactory.class);
    }
}