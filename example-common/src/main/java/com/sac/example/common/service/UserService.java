package com.sac.example.common.service;


import com.sac.example.common.model.User;

public interface UserService {

    User getUser(User user);

    default short getNum(){
        return 1;
    }

}
