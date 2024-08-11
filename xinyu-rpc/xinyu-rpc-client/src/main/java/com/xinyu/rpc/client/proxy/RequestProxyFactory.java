
package com.xinyu.rpc.client.proxy;

import com.xinyu.rpc.proxy.ProxyFactory;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;


@Component
@Slf4j
public class RequestProxyFactory implements ProxyFactory {

    /**
     * 创建新的代理实例-CGLib动态代理
     * @param cls
     * @param <T>
     * @return
     */
    //TODO CGLib动态代理执行“Enhancer enhancer = new Enhancer();”代码会报错
    /*@Override
    public  <T> T newProxyInstance(Class<T> cls) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cls);
        enhancer.setCallback(new CglibProxyCallBackHandler());
        return (T) enhancer.create();
    }*/


    /**
     * 创建新的代理实例-JDK动态代理
     * @param cls
     * @return
     * @param <T>
     */
    @Override
    public  <T> T newProxyInstance(Class<T> cls) {

        return (T) Proxy.newProxyInstance(
                cls.getClassLoader(),
                new Class[]{cls},
                new JDKProxyCallBackHandler()
        );
    }
}
