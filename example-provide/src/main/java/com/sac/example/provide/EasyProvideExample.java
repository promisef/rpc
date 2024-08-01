package com.sac.example.provide;

import com.sac.example.common.service.UserService;
import com.sac.rpc.RpcApplication;
import com.sac.rpc.registry.LocalRegistry;
import com.sac.rpc.server.HttpServer;
import com.sac.rpc.server.VertxHttpServer;


public class EasyProvideExample {
    public static void main(String[] args) {

        RpcApplication.init();
        //注册本地服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);


        HttpServer httpServer = new VertxHttpServer();
        httpServer.dostart(RpcApplication.getRpcConfig().getServerPort());
    }
}
