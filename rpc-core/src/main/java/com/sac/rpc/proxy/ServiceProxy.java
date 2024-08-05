//package com.sac.rpc.proxy;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.http.HttpRequest;
//import cn.hutool.http.HttpResponse;
//import com.sac.rpc.RpcApplication;
//import com.sac.rpc.config.RpcConfig;
//import com.sac.rpc.constans.RpcConstant;
//import com.sac.rpc.model.RpcRequest;
//import com.sac.rpc.model.RpcResponse;
//import com.sac.rpc.model.ServiceMetaInfo;
//import com.sac.rpc.protocol.ProtocolConstant;
//import com.sac.rpc.protocol.ProtocolMessage;
//import com.sac.rpc.protocol.ProtocolMessageSerializerEnum;
//import com.sac.rpc.registry.Registry;
//import com.sac.rpc.registry.RegistryFactory;
//import com.sac.rpc.serializer.JdkSerializer;
//import com.sac.rpc.serializer.Serializer;
//import com.sac.rpc.serializer.SerializerFactory;
//import io.vertx.core.Vertx;
//import io.vertx.core.net.NetClient;
//
//import java.io.IOException;
//import java.lang.reflect.InvocationHandler;
//import java.lang.reflect.Method;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//
//public class ServiceProxy implements InvocationHandler {
//
//    @Override
//    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
//
//        // 构造请求
//        String serviceName = method.getDeclaringClass().getName();
//        RpcRequest rpcRequest = RpcRequest.builder()
//                .serviceName(serviceName)
//                .methodName(method.getName())
//                .parameterTypes(method.getParameterTypes())
//                .params(args)
//                .build();
//        try {
//            // 序列化
//            byte[] bodyBytes = serializer.serialize(rpcRequest);
//            // 发送请求
//            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
//            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
//            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
//            serviceMetaInfo.setServiceName(serviceName);
//            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
//            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
//            if (CollUtil.isEmpty(serviceMetaInfoList)) {
//                throw new RuntimeException("暂无服务地址");
//            }
//            ServiceMetaInfo serviceMetaInfo1 = serviceMetaInfoList.get(0);
////            try (HttpResponse httpResponse = HttpRequest.post(serviceMetaInfo1.getServiceAddress())
////                    .body(bodyBytes)
////                    .execute()) {
////                byte[] result = httpResponse.bodyBytes();
////                // 反序列化
////                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
////                return rpcResponse.getData();
////            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}
package com.sac.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;

import com.sac.rpc.LoadBalance.LoadBalancer;
import com.sac.rpc.LoadBalance.LoadBalancerFactory;
import com.sac.rpc.RpcApplication;
import com.sac.rpc.config.RpcConfig;
import com.sac.rpc.constans.RpcConstant;
import com.sac.rpc.model.RpcRequest;
import com.sac.rpc.model.RpcResponse;
import com.sac.rpc.model.ServiceMetaInfo;
import com.sac.rpc.protocol.*;
import com.sac.rpc.registry.Registry;
import com.sac.rpc.registry.RegistryFactory;
import com.sac.rpc.serializer.Serializer;
import com.sac.rpc.serializer.SerializerFactory;

import com.sac.rpc.server.tcp.VertxTcpClient;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;


import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 服务代理（JDK 动态代理）
 *
 */
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .params(args)
                .build();
        try {
            // 序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            // 从注册中心获取服务提供者请求地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("暂无服务地址");
            }
            // 发送 TCP 请求
//            Vertx vertx = Vertx.vertx();
//            NetClient netClient = vertx.createNetClient();
//            CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
//            netClient.connect(selectedServiceMetaInfo.getServicePort(), selectedServiceMetaInfo.getServiceHost(),
//                    result -> {
//                        if (result.succeeded()) {
//                            System.out.println("Connected to TCP server");
//                            io.vertx.core.net.NetSocket socket = result.result();
//                            // 发送数据
//                            // 构造消息
//                            ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
//                            ProtocolMessage.Header header = new ProtocolMessage.Header();
//                            header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
//                            header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
//                            header.setSerializer((byte) ProtocolMessageSerializerEnum.getEnumByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
//                            header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
//                            header.setRequestId(IdUtil.getSnowflakeNextId());
//                            protocolMessage.setHeader(header);
//                            protocolMessage.setBody(rpcRequest);
//                            // 编码请求
//                            try {
//                                Buffer encodeBuffer = ProtocolMessageEncoder.encode(protocolMessage);
//                                socket.write(encodeBuffer);
//                            } catch (IOException e) {
//                                throw new RuntimeException("协议消息编码错误");
//                            }
//
//                            // 接收响应
//                            socket.handler(buffer -> {
//                                try {
//                                    ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
//                                    responseFuture.complete(rpcResponseProtocolMessage.getBody());
//                                } catch (IOException e) {
//                                    throw new RuntimeException("协议消息解码错误");
//                                }
//                            });
//                        } else {
//                            System.err.println("Failed to connect to TCP server");
//                        }
//                    });
//
//            RpcResponse rpcResponse = responseFuture.get();
//            // 记得关闭连接
//            netClient.close();

            LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
            // 将调用方法名（请求路径）作为负载均衡参数
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("methodName", rpcRequest.getMethodName());
            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);
            System.out.println("selectedServiceMetaInfo地址"+selectedServiceMetaInfo.getServiceAddress());


            RpcResponse rpcResponse = VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo);

            return rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

