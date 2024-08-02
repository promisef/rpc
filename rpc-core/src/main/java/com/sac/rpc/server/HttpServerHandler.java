package com.sac.rpc.server;


import com.sac.rpc.RpcApplication;
import com.sac.rpc.model.RpcRequest;
import com.sac.rpc.model.RpcResponse;
import com.sac.rpc.registry.LocalRegistry;
import com.sac.rpc.serializer.JdkSerializer;
import com.sac.rpc.serializer.Serializer;
import com.sac.rpc.serializer.SerializerFactory;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.lang.reflect.Method;

public class HttpServerHandler implements Handler<HttpServerRequest> {

    @Override
    public void handle(HttpServerRequest request) {

        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
        System.out.println("Received request: " + request.method() + request.uri());

        request.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                rpcRequest =serializer.deserialize(bytes, RpcRequest.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            RpcResponse rpcResponse = new RpcResponse();
            if(rpcRequest == null){
                rpcResponse.setMessage("Invalid request null");
                doResponse(request, rpcResponse, serializer);
            }

            try {
                Class<?> Implclass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = Implclass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object res = method.invoke(Implclass.newInstance(), rpcRequest.getParams());

                rpcResponse.setData(res);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("ok");
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }
            doResponse(request, rpcResponse, serializer);
        });
    }

    void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer){
        HttpServerResponse response = request.response()
                .putHeader("content-type", "application/json");

        try {
            byte[] bytes = serializer.serialize(rpcResponse);
            response.end(Buffer.buffer(bytes));
        } catch (Exception e) {
            e.printStackTrace();
            response.end(Buffer.buffer());
        }

    }
}
