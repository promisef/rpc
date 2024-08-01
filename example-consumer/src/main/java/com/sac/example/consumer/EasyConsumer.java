package com.sac.example.consumer;

import com.sac.example.common.model.User;
import com.sac.example.common.service.UserService;
import com.sac.rpc.proxy.ServiceProxyFactory;

public class EasyConsumer {
    public static void main(String[] args) {

//        UserService userService = new UserServiceProxy();
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("sac");
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        }else{
            System.out.println("user == null");
        }
    }

}
