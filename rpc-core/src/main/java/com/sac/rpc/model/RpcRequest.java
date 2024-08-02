package com.sac.rpc.model;


import com.sac.rpc.constans.RpcConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {

    private String serviceName;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] params;

    private String serviceVersion = RpcConstant.DEFAULT_SERVICE_VERSION;

}
