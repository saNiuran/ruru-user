package com.ruru.plastic.user.service;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.model.Role;
import com.ruru.plastic.user.request.RoleRequest;

import java.util.List;

public interface RoleService {
    Role getRoleById(Long id);

    Role getRoleByName(String name);

    Msg<Role> createRole(Role role);

    Msg<Role> updateRole(Role role);

    Msg<Role> deleteRole(Role role);

    List<Role> getRoleList();

    PageInfo<Role> filterPagedRoleList(RoleRequest request);
}
