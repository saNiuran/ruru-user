package com.ruru.plastic.user.controller.admin;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.enume.StatusEnum;
import com.ruru.plastic.user.enume.UserTypeEnum;
import com.ruru.plastic.user.model.AdminUser;
import com.ruru.plastic.user.model.Role;
import com.ruru.plastic.user.model.RoleMatch;
import com.ruru.plastic.user.model.User;
import com.ruru.plastic.user.net.AdminRequired;
import com.ruru.plastic.user.net.CurrentAdminUser;
import com.ruru.plastic.user.net.CurrentUser;
import com.ruru.plastic.user.net.LoginRequired;
import com.ruru.plastic.user.redis.RedisService;
import com.ruru.plastic.user.request.AdminUserRequest;
import com.ruru.plastic.user.response.AdminUserResponse;
import com.ruru.plastic.user.response.DataResponse;
import com.ruru.plastic.user.service.AdminUserService;
import com.ruru.plastic.user.service.RoleMatchService;
import com.ruru.plastic.user.service.RoleService;
import com.ruru.plastic.user.task.TokenTask;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/admin/user")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private TokenTask tokenTask;
    @Autowired
    private RoleMatchService roleMatchService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RoleService roleService;

    @PostMapping("/login")
    public DataResponse<AdminUserResponse> adminLogin(HttpServletRequest servletRequest, @RequestBody AdminUserRequest request) {
        if (request == null || StringUtils.isEmpty(request.getUserName())
                || StringUtils.isEmpty(request.getPassword())) {
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        if (StringUtils.isEmpty(servletRequest.getHeader("appType")) || StringUtils.isEmpty(servletRequest.getHeader("deviceCode"))) {
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }

        String appType = servletRequest.getHeader("appType");
        String deviceCode = servletRequest.getHeader("deviceCode");

        AdminUser adminUserByUserName = adminUserService.getAdminUserByUserName(request.getUserName());

        if (adminUserByUserName == null) {
            return DataResponse.error(Constants.ERROR_NO_INFO);
        } else {
            if (adminUserByUserName.getStatus().equals(StatusEnum.不可用.getValue())) {
                return DataResponse.error("账号被停用！");
            } else if (!adminUserByUserName.getPassword().equals(request.getPassword())) {
                return DataResponse.error("密码错误！");
            }

            String token = tokenTask.createToken(adminUserByUserName.getId(), Integer.parseInt(appType), deviceCode, UserTypeEnum.Admin.getValue());

            if (StringUtils.isEmpty(token)) {
                return DataResponse.error("token创建失败！");
            }

            AdminUserResponse response = adminUserService.getAdminUserResponseById(adminUserByUserName.getId());
            assert response != null;
            response.setPassword(null);
            response.setAdminToken(token);
            return DataResponse.success(response);
        }
    }

    @AdminRequired
    @PostMapping("/info")
    public DataResponse<AdminUserResponse> getAdminUserById(@RequestBody AdminUser adminUser) {
        if (adminUser == null || adminUser.getId() == null) {
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        AdminUser adminUserById = adminUserService.getAdminUserById(adminUser.getId());
        if (adminUserById == null) {
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }
        AdminUserResponse response = new AdminUserResponse();
        adminUserById.setPassword(null);
        BeanUtils.copyProperties(adminUserById, response);
        return DataResponse.success(response);
    }


    @AdminRequired
    @PostMapping("/filter")
    public DataResponse<PageInfo<AdminUserResponse>> filterPagedAdminUserList(@RequestBody AdminUserRequest adminUserRequest) {
        PageInfo<AdminUser> adminUserPageInfo = adminUserService.filterPagedAdminUserList(adminUserRequest);
        PageInfo<AdminUserResponse> responsePageInfo = new PageInfo<>();
        BeanUtils.copyProperties(adminUserPageInfo, responsePageInfo);

        List<AdminUserResponse> responseList = new ArrayList<>();

        for (AdminUser adminUser : adminUserPageInfo.getList()) {
            responseList.add(adminUserService.getAdminUserResponseById(adminUser.getId()));
        }
        responsePageInfo.setList(responseList);
        return DataResponse.success(responsePageInfo);
    }

    @AdminRequired
    @PostMapping("/new")
    public DataResponse<AdminUserResponse> newAdminUser(@RequestBody AdminUserRequest request) {
        AdminUser adminUser = new AdminUser();
        BeanUtils.copyProperties(request, adminUser);
        Msg<AdminUser> msg = adminUserService.createAdminUser(adminUser);
        if (StringUtils.isNotEmpty(msg.getErrorMsg())) {
            return DataResponse.error(msg.getErrorMsg());
        }

        AdminUser dbAdminUser = msg.getData();

        updateRoleMatch(request, dbAdminUser);
        return DataResponse.success(adminUserService.getAdminUserResponseById(dbAdminUser.getId()));
    }

    @AdminRequired
    @PostMapping("/delete")
    public DataResponse<AdminUserResponse> deleteAdminUser(@CurrentAdminUser AdminUser user, @RequestBody AdminUser adminUser) {
        if (adminUser == null || adminUser.getId() == null) {
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        if (adminUser.getId().equals(user.getId())) {
            return DataResponse.error("不能删除自己账户！");
        }
        Msg<AdminUser> adminUserMsg = adminUserService.deleteAdminUser(adminUser.getId());
        if (StringUtils.isNotEmpty(adminUserMsg.getErrorMsg())) {
            return DataResponse.error(adminUserMsg.getErrorMsg());
        }
        AdminUser dbAdminUser = adminUserMsg.getData();
        return DataResponse.success(adminUserService.getAdminUserResponseById(dbAdminUser.getId()));
    }

    @AdminRequired
    @PostMapping("/update")
    public DataResponse<AdminUser> updateAdminUser(@RequestBody AdminUserRequest request) {
        AdminUser adminUser = new AdminUser();
        BeanUtils.copyProperties(request, adminUser);
        Msg<AdminUser> msg = adminUserService.updateAdminUser(adminUser);
        if (StringUtils.isNotEmpty(msg.getErrorMsg())) {
            return DataResponse.error(msg.getErrorMsg());
        }
        AdminUser dbAdminUser = msg.getData();
        if (request.getRoleForce() != null && request.getRoleForce().equals(StatusEnum.可用.getValue())) {
            updateRoleMatch(request, dbAdminUser);
        }
        return DataResponse.success(adminUserService.getAdminUserResponseById(dbAdminUser.getId()));
    }

    private void updateRoleMatch(AdminUserRequest request, AdminUser dbAdminUser) {
        //更改Role
        List<RoleMatch> oldRoleMatch = roleMatchService.getRoleMatchesByAdminUserId(dbAdminUser.getId());   //旧的配置
        List<Long> oldRoleMatchRoleIds = oldRoleMatch.stream().map(RoleMatch::getRoleId).collect(Collectors.toList());

        List<Role> roles = request.getRoles();
        List<Long> newRoleIds = new ArrayList<>();
        if (roles != null && roles.size() > 0) {
            newRoleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
        }
        List<Long> finalRoleIds = newRoleIds;   //新的配置
        //新配置中不包含的，删除
        List<RoleMatch> collect = oldRoleMatch.stream().filter(v -> !finalRoleIds.contains(v.getRoleId())).collect(Collectors.toList());
        for (RoleMatch roleMatch : collect) {
            roleMatchService.deleteRoleMatch(roleMatch);
        }
        //旧配置中没有的，添加
        List<Long> collect1 = newRoleIds.stream().filter(v -> !oldRoleMatchRoleIds.contains(v)).collect(Collectors.toList());
        for (Long roleId : collect1) {
            RoleMatch match = new RoleMatch();
            match.setRoleId(roleId);
            match.setAdminUserId(dbAdminUser.getId());
            roleMatchService.createRoleMatch(match);
        }
    }

    @PostMapping("/logout")
    public DataResponse<Void> adminLogout(@RequestBody AdminUser adminUser) {
        if(adminUser==null || adminUser.getId()==null){
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        AdminUser adminUserById = adminUserService.getAdminUserById(adminUser.getId());
        if(adminUserById!=null){
            redisService.expireUserInfo(adminUser.getId(),UserTypeEnum.Admin.getValue());
        }
        return DataResponse.success(null);
    }

    @AdminRequired
    @PostMapping("/info/current")
    public DataResponse<AdminUserResponse> getCurrentAdminUser(@CurrentAdminUser AdminUser adminUser) {
        return DataResponse.success(adminUserService.getAdminUserResponseById(adminUser.getId()));
    }

    @AdminRequired
    @PostMapping("/info/current/simple")
    public DataResponse<AdminUser> getCurrentAdmin(@CurrentAdminUser AdminUser adminUser) {
        return DataResponse.success(adminUser);
    }

    @PostMapping("/query/by/role/name")
    public DataResponse<List<AdminUser>> queryUserIdByRoleId(@RequestBody Role role){
        if(role==null || StringUtils.isEmpty(role.getName())){
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        Role roleById = roleService.getRoleByName(role.getName());
        if(roleById==null || roleById.getStatus().equals(StatusEnum.不可用.getValue())){
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }

        List<RoleMatch> matchList = roleMatchService.listRoleMatchByRoleId(roleById.getId());
        List<AdminUser> adminUserList = new ArrayList<>();

        for(RoleMatch match: matchList){
            AdminUser adminUserById = adminUserService.getAdminUserById(match.getAdminUserId());
            if(adminUserById!=null && adminUserById.getStatus().equals(StatusEnum.可用.getValue())){
                adminUserList.add(adminUserById);
            }
        }
        return DataResponse.success(adminUserList);
    }
}
