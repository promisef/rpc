package com.sac.rpc.LoadBalance;

import com.sac.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinLoadBalancer implements LoadBalancer {


    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> services) {
        if(services == null) return null;
        int size = services.size();
        if(size == 1) return services.get(0);
        int index = counter.getAndIncrement() % size;
        return services.get(index);
    }
}
