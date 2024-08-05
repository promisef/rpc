package com.sac.rpc.LoadBalance;

import com.sac.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class RandomLoadBalancer implements LoadBalancer {

    private final Random random = new Random();

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> services) {
        int size = services.size();
        if (size == 0) return null;
        if (size == 1) return services.get(0);
        return services.get(random.nextInt(size));
    }
}
