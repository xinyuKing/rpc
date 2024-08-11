
package com.xinyu.rpc.proxy;

public interface ProxyFactory {

    public  <T> T newProxyInstance(Class<T> cls) ;
}
