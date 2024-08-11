
package com.xinyu.rpc.client.route;

import com.xinyu.rpc.provider.ServiceProvider;

import java.util.List;

public interface ServiceRoute {

    List<ServiceProvider> route(List<ServiceProvider> serviceProviderList);
}
