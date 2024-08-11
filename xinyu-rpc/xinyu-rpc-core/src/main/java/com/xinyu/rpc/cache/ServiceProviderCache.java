
package com.xinyu.rpc.cache;



import com.xinyu.rpc.provider.ServiceProvider;

import java.util.List;

public interface ServiceProviderCache {
    /**
     * 向缓存中添加数据
     * @param key
     * @param value
     */
    void put(String key, List<ServiceProvider> value);

    /**
     * 获取缓存
     * @param key
     * @return
     */
    List<ServiceProvider> get(String key);

    /**
     * 缓存清除
     * @param key
     */
    void evict(String key);


    /**
     * 缓存更新
     * @param key
     * @param value
     */
    void update(String key,List<ServiceProvider> value);
}
