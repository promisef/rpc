package com.sac.rpc.LoadBalance;

import com.sac.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

public interface LoadBalancer {

    ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> services);
}
