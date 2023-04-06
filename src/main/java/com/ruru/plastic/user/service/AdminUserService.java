package com.ruru.plastic.user.service;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.model.AdminUser;
import com.ruru.plastic.user.request.AdminUserRequest;
import com.ruru.plastic.user.response.AdminUserResponse;

public interface AdminUserService {
    AdminUser getAdminUserById(Long userId);

    AdminUserResponse getAdminUserResponseById(Long userId);

    Msg<AdminUser> updateAdminUser(AdminUser adminUser);

    PageInfo<AdminUser> filterPagedAdminUserList(AdminUserRequest adminUserRequest);

    Msg<AdminUser> createAdminUser(AdminUser adminUser);

    Msg<AdminUser> deleteAdminUser(Long userId);

    AdminUser getAdminUserByUserName(String userName);

    AdminUser getAdminUserByUser(Long userId);
}
