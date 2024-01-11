package com.ruru.plastic.user.service;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.model.UserProperty;
import com.ruru.plastic.user.request.UserPropertyRequest;

import java.util.List;

public interface UserPropertyService {
    UserProperty getUserPropertyById(Long id);

    List<UserProperty> queryUserProperty(UserPropertyRequest request);

    Msg<UserProperty> createUserProperty(UserProperty userProperty);

    Msg<UserProperty> updateUserProperty(UserProperty userProperty);

    Msg<UserProperty> deleteUserProperty(UserProperty userProperty);

    PageInfo<UserProperty> filterUserProperty(UserPropertyRequest request);
}
