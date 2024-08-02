package com.sac.rpc.config;

import com.sac.rpc.serializer.SerializerKeys;
import lombok.Data;

@Data
public class RpcConfig {

    private String name = "sac-rpc";

    private String version = "1.0";

    private String serverHost = "localhost";

    private Integer serverPort = 8080;

    private boolean mock = false;

    private String serializer = SerializerKeys.JSON;

    private RegistryConfig registryConfig = new RegistryConfig();
}
