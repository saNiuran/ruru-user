package com.ruru.plastic.user.service;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.model.User;
import com.ruru.plastic.user.request.UserRequest;

import java.util.List;

public interface UserService {
    User getUserById(Long userId);

    User getUserByAdminUserId(Long adminId);

    User getUserByMobile(String mobile);

    User getValidUserByMobile(String mobile);

    //创建用户，手机号不能重复
    Msg<User> createUser(User user);

    Msg<User> updateUser(User user);

    Msg<User> deleteUser(User user);

    PageInfo<User> filterUser(UserRequest request);

    Msg<List<User>> searchUser(UserRequest request);

    Msg<List<String>> queryMobilesByUserIds(List<Long> userIdList);

}
