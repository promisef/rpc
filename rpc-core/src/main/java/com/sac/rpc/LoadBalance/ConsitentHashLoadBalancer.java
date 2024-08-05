package com.sac.rpc.LoadBalance;

import com.sac.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ConsitentHashLoadBalancer implements LoadBalancer {

    private final TreeMap<Integer, ServiceMetaInfo> virtualNodes = new TreeMap<>();

    private static final int VIRTUAL_NODE_SIZE = 50;

    private int getHash(Object key){
        return key.hashCode();
    }


    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> services) {
        if(services == null){
            return null;
        }
        for(ServiceMetaInfo serviceMetaInfo : services){
            for(int i = 0; i<VIRTUAL_NODE_SIZE; i++){
                int hash = getHash(serviceMetaInfo.getServiceAddress() + "#" + i);
                virtualNodes.put(hash, serviceMetaInfo);
            }
        }

        int hash = getHash(requestParams);

        // 选择最接近且大于等于调用请求 hash 值的虚拟节点
        Map.Entry<Integer, ServiceMetaInfo> entry = virtualNodes.ceilingEntry(hash);
        if (entry == null) {
            // 如果没有大于等于调用请求 hash 值的虚拟节点，则返回环首部的节点
            entry = virtualNodes.firstEntry();
        }
        return entry.getValue();
    }
}
