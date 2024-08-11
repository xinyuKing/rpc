
package com.xinyu.rpc.client.config;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import com.xinyu.rpc.provider.ServiceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BeanConfig {

    @Bean
    public LoadingCache<String, List<ServiceProvider>> buildCache() {
        return CacheBuilder.newBuilder()
                .build(new CacheLoader<String, List<ServiceProvider>>() {
                    @Override
                    public List<ServiceProvider> load(String key) throws Exception {
                        //在这里可以初始化加载数据的缓存信息，读取数据库中信息或者是加载文件中的某些数据信息
                        return null;
                    }
                });
    }
}
