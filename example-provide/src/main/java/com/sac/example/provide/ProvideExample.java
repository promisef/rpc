package com.sac.example.provide;

import com.sac.example.common.service.UserService;
import com.sac.rpc.RpcApplication;
import com.sac.rpc.config.RegistryConfig;
import com.sac.rpc.config.RpcConfig;
import com.sac.rpc.model.ServiceMetaInfo;
import com.sac.rpc.registry.LocalRegistry;
import com.sac.rpc.registry.Registry;
import com.sac.rpc.registry.RegistryFactory;
import com.sac.rpc.server.HttpServer;
import com.sac.rpc.server.VertxHttpServer;

public class ProvideExample {

    public static void main(String[] args) {
        RpcApplication.init();

        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        HttpServer httpServer = new VertxHttpServer();
        httpServer.dostart(RpcApplication.getRpcConfig().getServerPort());
    }
}
