package com.sac.rpc;

import com.sac.rpc.config.RpcConfig;
import com.sac.rpc.constans.RpcConstant;
import com.sac.rpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcApplication {

    private static volatile RpcConfig rpcConfig;


    public static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        log.info("RpcApplication init");
    }

    public static void init(){
        RpcConfig newRpcConfig;
        try{
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    public static RpcConfig getRpcConfig() {
        if(rpcConfig == null) {
            synchronized (RpcApplication.class) {
                if(rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
