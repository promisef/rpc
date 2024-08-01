package com.sac.example.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.sac.example.common.model.User;
import com.sac.example.common.service.UserService;
import com.sac.rpc.model.RpcRequest;
import com.sac.rpc.model.RpcResponse;
import com.sac.rpc.serializer.JdkSerializer;
import com.sac.rpc.serializer.Serializer;

import java.io.IOException;

public class UserServiceProxy implements UserService {

    @Override
    public User getUser(User user) {
        final Serializer serializer = new JdkSerializer();

        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .parameterTypes(new Class[]{User.class})
                .methodName("getUser")
                .params(new Object[]{user})
                .build();

        try {
            byte[] bodydates = serializer.serialize(rpcRequest);
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
                    .body(bodydates)
                    .execute()){
                byte[] result = httpResponse.bodyBytes();
                // 反序列化（字节数组 => Java 对象）
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                return (User) rpcResponse.getData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
