package com.ruru.plastic.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.dao.AdminUserMapper;
import com.ruru.plastic.user.enume.StatusEnum;
import com.ruru.plastic.user.model.AdminUser;
import com.ruru.plastic.user.model.AdminUserExample;
import com.ruru.plastic.user.request.AdminUserRequest;
import com.ruru.plastic.user.response.AdminUserResponse;
import com.ruru.plastic.user.response.RoleMatchResponse;
import com.ruru.plastic.user.service.AdminUserService;
import com.ruru.plastic.user.service.RoleMatchService;
import com.ruru.plastic.user.utils.NullUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private AdminUserMapper adminUserMapper;
    @Autowired
    private RoleMatchService roleMatchService;

    @Override
    public AdminUser getAdminUserById(Long userId) {
        return adminUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public AdminUserResponse getAdminUserResponseById(Long userId) {
        AdminUser adminUserById = getAdminUserById(userId);
        if(adminUserById==null){
            return null;
        }
        AdminUserResponse response = new AdminUserResponse();
        BeanUtils.copyProperties(adminUserById,response);
        response.setRoles(roleMatchService.getRoleMatchResponsesByAdminUserId(userId)
                .stream().map(RoleMatchResponse::getRole).collect(Collectors.toList()));
        response.setPassword(null);
        return response;
    }

    @Override
    public Msg<AdminUser> updateAdminUser(AdminUser adminUser) {
        if(adminUser==null || adminUser.getId()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        AdminUser dbAdminUser = adminUserMapper.selectByPrimaryKey(adminUser.getId());
        if(dbAdminUser==null){
            return Msg.error(Constants.ERROR_NO_INFO);
        }

        BeanUtils.copyProperties(adminUser,dbAdminUser, NullUtil.getNullPropertyNames(adminUser));

        //看用户名是否重复
        AdminUserExample adminUserExample = new AdminUserExample();
        adminUserExample.createCriteria()
                .andUserNameEqualTo(dbAdminUser.getUserName())
                .andIdNotEqualTo(dbAdminUser.getId());
        if (adminUserMapper.countByExample(adminUserExample)>0) {
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }
        dbAdminUser.setUpdateTime(new Date());

        adminUserMapper.updateByPrimaryKeySelective(dbAdminUser);
        return Msg.success(dbAdminUser);
    }

    //超级管理员 不在搜索之列
    @Override
    public PageInfo<AdminUser> filterPagedAdminUserList(AdminUserRequest request) {
        AdminUserExample example = new AdminUserExample();
        AdminUserExample.Criteria criteria = example.createCriteria();

        if (request.getId() != null) {
            criteria.andIdEqualTo(request.getId());
        }
        if (StringUtils.isNotEmpty(request.getUserName())) {
            criteria.andUserNameEqualTo(request.getUserName());
        }
        if (request.getStatus() != null) {
            criteria.andStatusEqualTo(request.getStatus());
        }
        if(StringUtils.isNotEmpty(request.getName())){
            criteria.andNameEqualTo(request.getName());
        }
        if(StringUtils.isNotEmpty(request.getMobile())){
            criteria.andMobileEqualTo(request.getMobile());
        }
        if (request.getStartTime() != null) {
            criteria.andCreateTimeGreaterThanOrEqualTo(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            criteria.andCreateTimeLessThanOrEqualTo(request.getEndTime());
        }
        if (StringUtils.isNotEmpty(request.getSearch())) {
            criteria.andNameLike("%" + request.getSearch() + "%");
        }
        if (StringUtils.isNotEmpty(request.getOrderClause())) {
            example.setOrderByClause(request.getOrderClause());
        }

        PageHelper.startPage(request.getPage(), request.getSize());
        return new PageInfo<>(adminUserMapper.selectByExample(example));
    }

    @Override
    public Msg<AdminUser> createAdminUser(AdminUser adminUser) {
        if (adminUser == null || StringUtils.isEmpty(adminUser.getUserName())) {
            return Msg.error(Constants.ERROR_PARAMETER);
        }

        AdminUser user = getAdminUserByUserName(adminUser.getUserName());
        if (user != null) {
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }

        if(adminUser.getStatus()==null) {
            adminUser.setStatus(StatusEnum.可用.getValue());
        }
        if(adminUser.getCreateTime()==null){
            adminUser.setCreateTime(new Date());
        }
        if(adminUser.getUpdateTime()==null){
            adminUser.setUpdateTime(new Date());
        }
        adminUserMapper.insertSelective(adminUser);
        return Msg.success(getAdminUserById(adminUser.getId()));
    }

    @Override
    public Msg<AdminUser> deleteAdminUser(Long userId) {
        if (userId == null) {
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        AdminUser adminUser = getAdminUserById(userId);
        if (adminUser == null) {
            return Msg.error(Constants.ERROR_NO_INFO);
        }
        adminUser.setStatus(StatusEnum.不可用.getValue());
        adminUser.setUpdateTime(new Date());
        adminUserMapper.updateByPrimaryKeySelective(adminUser);
        return Msg.success(getAdminUserById(userId));
    }

    @Override
    public AdminUser getAdminUserByUserName(String userName) {
        AdminUserExample example = new AdminUserExample();
        example.createCriteria().andUserNameEqualTo(userName);
        return adminUserMapper.selectOneByExample(example);
    }

    @Override
    public AdminUser getAdminUserByUser(Long userId) {
        AdminUserExample example = new AdminUserExample();
        example.createCriteria().andStatusEqualTo(StatusEnum.可用.getValue());
        return adminUserMapper.selectOneByExample(example);
    }


}
