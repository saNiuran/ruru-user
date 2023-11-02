package com.ruru.plastic.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.dao.UserMapper;
import com.ruru.plastic.user.enume.*;
import com.ruru.plastic.user.model.User;
import com.ruru.plastic.user.model.UserExample;
import com.ruru.plastic.user.request.UserRequest;
import com.ruru.plastic.user.service.UserService;
import com.ruru.plastic.user.utils.MyStringUtils;
import com.ruru.plastic.user.utils.NullUtil;
import com.xiaoleilu.hutool.util.RandomUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public User getUserByAdminUserId(Long adminId) {
        UserExample example = new UserExample();
        example.createCriteria().andAdminIdEqualTo(adminId);
        return userMapper.selectOneByExample(example);
    }

    @Override
    public User getUserByMobile(String mobile){
        UserExample example = new UserExample();
        example.createCriteria().andMobileEqualTo(mobile);
        return userMapper.selectOneByExample(example);
    }
    @Override
    public User getValidUserByMobile(String mobile){
        UserExample example = new UserExample();
        example.createCriteria().andMobileEqualTo(mobile).andStatusEqualTo(UserStatusEnum.有效.getValue());
        return userMapper.selectOneByExample(example);
    }

    @Override
    public List<User> queryUser(User user){
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();

        UserRequest request = new UserRequest();
        BeanUtils.copyProperties(user,request);

        queryUser(request,criteria);

        return userMapper.selectByExample(example);
    }

    //创建用户，手机号不能重复; 不自动设置用户为公司第一联系人
    @Override
    public Msg<User> createUser(User user) {
        if (user==null || StringUtils.isEmpty(user.getMobile())) {
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        User dbUser = getValidUserByMobile(user.getMobile());
        if (dbUser != null) {
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }

        user.setId(null);
        if(user.getCompanyId()==null){
            user.setPremier(null);
        }else{
            user.setPremier(StatusEnum.不可用.getValue());
        }
        if (user.getStatus() == null) {
            user.setStatus(UserStatusEnum.有效.getValue());
        }
        if (StringUtils.isEmpty(user.getNickName())) {
            user.setNickName("如如" + RandomUtil.randomNumbers(5));
        }
        if(user.getGender()==null){
            user.setGender(UserGenderEnum.保密.getValue());
        }
        if(user.getMemberLevel()==null){
            user.setMemberLevel(UserMemberLevelEnum.普通用户.getValue());
        }
        if(user.getCertLevel()==null){
            user.setCertLevel(UserCertLevelEnum.未认证.getValue());
        }
        if(user.getPushStatus()==null){
            user.setPushStatus(StatusEnum.可用.getValue());
        }
        if (user.getCreateTime() == null) {
            user.setCreateTime(new Date());
        }
        if (user.getUpdateTime() == null) {
            user.setUpdateTime(new Date());
        }

        userMapper.insertSelective(user);

        return Msg.success(getUserById(user.getId()));
    }

    @Override
    public Msg<User> updateUser(User user) {
        if (user == null || user.getId() == null) {
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        User dbUser = getUserById(user.getId());
        if (dbUser == null) {
            return Msg.error(Constants.ERROR_NO_INFO);
        }

        BeanUtils.copyProperties(user, dbUser, NullUtil.getNullPropertyNames(user));

        //防止修改后有重复的电话号码
        if(StringUtils.isNotEmpty(user.getMobile())) {
            UserExample example = new UserExample();
            example.createCriteria().andMobileEqualTo(dbUser.getMobile()).andIdNotEqualTo(user.getId());
            if (userMapper.countByExample(example) > 0) {
                return Msg.error(Constants.ERROR_DUPLICATE_INFO);
            }
        }
        dbUser.setUpdateTime(new Date());
        userMapper.updateByPrimaryKeySelective(dbUser);
        return Msg.success(getUserById(dbUser.getId()));
    }

    @Override
    public Msg<User> deleteUser(@NotNull User user) {
        user.setStatus(UserStatusEnum.注销.getValue());
        return updateUser(user);
    }

    @Override
    public PageInfo<User> filterUser(UserRequest request) {

        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();

        queryUser(request, criteria);

        if (request.getStartTime() != null) {
            criteria.andUpdateTimeGreaterThanOrEqualTo(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            criteria.andUpdateTimeLessThanOrEqualTo(request.getEndTime());
        }

        if (StringUtils.isNotEmpty(request.getOrderClause())) {
            example.setOrderByClause(request.getOrderClause());
        } else {
            example.setOrderByClause("id desc");
        }
        PageHelper.startPage(request.getPage(), request.getSize());

        return new PageInfo<>(userMapper.selectByExample(example));
    }

    private static void queryUser(UserRequest request, UserExample.Criteria criteria) {
        if(request.getId()!=null){
            criteria.andIdEqualTo(request.getId());
        }
        if(request.getCompanyId()!=null){
            criteria.andCompanyIdEqualTo(request.getCompanyId());
        }
        if(StringUtils.isNotEmpty(request.getTitle())){
            criteria.andTitleEqualTo(request.getTitle());
        }
        if (StringUtils.isNotEmpty(request.getMobile())) {
            criteria.andMobileEqualTo(request.getMobile());
        }
        if (StringUtils.isNotEmpty(request.getNickName())) {
            criteria.andNickNameEqualTo(request.getNickName());
        }
        if (StringUtils.isNotEmpty(request.getSlogan())) {
            criteria.andSloganEqualTo(request.getSlogan());
        }
        if(StringUtils.isNotEmpty(request.getCareer())){
            criteria.andCareerEqualTo(request.getCareer());
        }
        if(StringUtils.isNotEmpty(request.getCity())){
            criteria.andCityEqualTo(request.getCity());
        }
        if(request.getGender()!=null){
            criteria.andGenderEqualTo(request.getGender());
        }
        if(request.getCertLevel()!=null){
            criteria.andCertLevelEqualTo(request.getCertLevel());
        }
        if(request.getMemberLevel()!=null){
            criteria.andMemberLevelEqualTo(request.getMemberLevel());
        }
        if(request.getPushStatus()!=null){
            criteria.andPushStatusEqualTo(request.getPushStatus());
        }
        if (request.getStatus() != null) {
            criteria.andStatusEqualTo(request.getStatus());
        }
        if(request.getAdminId()!=null){
            criteria.andAdminIdEqualTo(request.getAdminId());
        }
    }

    @Override
    public Msg<List<User>> searchUser(UserRequest request){
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();

        if(request.getIdList()!=null && request.getIdList().size()>0){
            criteria.andIdIn(request.getIdList());
        }
        criteria.andStatusEqualTo(UserStatusEnum.有效.getValue());

        if(MyStringUtils.isNumericAndLengthCheck(request.getSearch())){
            UserExample.Criteria criteria2 = example.createCriteria();
            BeanUtils.copyProperties(criteria,criteria2);
            criteria2.getCriteria().addAll(criteria.getAllCriteria());

            criteria.andMobileLike("%"+request.getSearch()+"%");
            criteria2.andNickNameLike("%"+request.getSearch()+"%");

            example.or(criteria2);
        }else{
            criteria.andNickNameLike("%"+request.getSearch()+"%");
        }

        List<User> userList = userMapper.selectByExample(example);
        if(userList.size()>500){
            return Msg.error("结果数据太大！");
        }
        return Msg.success(userList);
    }

    @Override
    public Msg<List<String>> queryMobilesByUserIds(List<Long> userIdList){
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        if(userIdList==null || userIdList.size()==0){
            return Msg.success(new ArrayList<>());
        }
        criteria.andIdIn(userIdList);

        return Msg.success(userMapper.selectByExample(example).stream().map(User::getMobile).collect(Collectors.toList()));
    }
}
