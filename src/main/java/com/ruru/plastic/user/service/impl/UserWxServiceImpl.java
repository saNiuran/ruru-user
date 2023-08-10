package com.ruru.plastic.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.dao.UserWxMapper;
import com.ruru.plastic.user.model.UserWx;
import com.ruru.plastic.user.model.UserWxExample;
import com.ruru.plastic.user.request.UserWxRequest;
import com.ruru.plastic.user.service.UserWxService;
import com.ruru.plastic.user.utils.NullUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserWxServiceImpl implements UserWxService {
    @Autowired
    private UserWxMapper userWxMapper;

    @Override
    public UserWx getUserWxById(Long id){
        return userWxMapper.selectByPrimaryKey(id);
    }

    @Override
    public UserWx getUserWxByOpenId(String openId){
        UserWxExample example= new UserWxExample();
        example.createCriteria().andOpenidEqualTo(openId);
        return userWxMapper.selectOneByExample(example);
    }

    @Override
    public List<UserWx> queryUserWx(UserWxRequest request){
        UserWxExample example = new UserWxExample();
        UserWxExample.Criteria criteria = example.createCriteria();

        queryUserWx(request, criteria);
        return userWxMapper.selectByExample(example);
    }

    @Override
    public Msg<UserWx> createUserWx(UserWx userWx){
        if(userWx==null || StringUtils.isEmpty(userWx.getOpenid()) || userWx.getType()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        UserWxExample example = new UserWxExample();
        example.createCriteria().andOpenidEqualTo(userWx.getOpenid()).andTypeEqualTo(userWx.getType());
        if(userWxMapper.countByExample(example)>0){
            userWx.setId(userWxMapper.selectOneByExample(example).getId());
            return updateUserWx(userWx);
        }
        userWx.setId(null);
        if(userWx.getCreateTime()==null){
            userWx.setCreateTime(new Date());
        }
        userWxMapper.insertSelective(userWx);
        return Msg.success(getUserWxById(userWx.getId()));
    }

    @Override
    public Msg<UserWx> updateUserWx(UserWx userWx){
        if(userWx==null || userWx.getId()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        UserWx userWxById = getUserWxById(userWx.getId());
        if(userWxById==null){
            return Msg.error(Constants.ERROR_NO_INFO);
        }

        BeanUtils.copyProperties(userWx,userWxById, NullUtil.getNullPropertyNames(userWx));
        UserWxExample example = new UserWxExample();
        example.createCriteria().andIdNotEqualTo(userWx.getId())
                .andOpenidEqualTo(userWxById.getOpenid())
                .andTypeEqualTo(userWxById.getType());
        if(userWxMapper.countByExample(example)>0){
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }
        userWxMapper.updateByPrimaryKeySelective(userWxById);
        return Msg.success(getUserWxById(userWxById.getId()));
    }

    @Override
    public Msg<Integer> deleteUserWx(UserWx userWx){
        if(userWx==null || userWx.getId()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        UserWx userWxById = getUserWxById(userWx.getId());
        if(userWxById==null){
            return Msg.error(Constants.ERROR_NO_INFO);
        }
        userWxMapper.deleteByPrimaryKey(userWx.getId());
        return Msg.success(1);
    }

    @Override
    public PageInfo<UserWx> filterUserWx(UserWxRequest request){
        UserWxExample example = new UserWxExample();
        UserWxExample.Criteria criteria = example.createCriteria();

        queryUserWx(request, criteria);

        example.setOrderByClause("create_time desc");

        PageHelper.startPage(request.getPage(),request.getSize());
        return new PageInfo<>(userWxMapper.selectByExample(example));
    }

    private static void queryUserWx(UserWxRequest request, UserWxExample.Criteria criteria) {
        if(request.getId()!=null){
            criteria.andIdEqualTo(request.getId());
        }
        if(StringUtils.isNotEmpty(request.getMobile())){
            criteria.andMobileEqualTo(request.getMobile());
        }
        if(StringUtils.isNotEmpty(request.getOpenid())){
            criteria.andOpenidEqualTo(request.getOpenid());
        }
        if(StringUtils.isNotEmpty(request.getUnionid())){
            criteria.andUnionidEqualTo(request.getUnionid());
        }
        if(StringUtils.isNotEmpty(request.getNickname())){
            criteria.andNicknameEqualTo(request.getNickname());
        }
        if(request.getSex()!=null){
            criteria.andSexEqualTo(request.getSex());
        }
        if(StringUtils.isNotEmpty(request.getLanguage())){
            criteria.andLanguageEqualTo(request.getLanguage());
        }
        if(StringUtils.isNotEmpty(request.getCity())){
            criteria.andCityEqualTo(request.getCity());
        }
        if(StringUtils.isNotEmpty(request.getProvince())){
            criteria.andProvinceEqualTo(request.getProvince());
        }
        if(StringUtils.isNotEmpty(request.getCountry())){
            criteria.andCountryEqualTo(request.getCountry());
        }
        if(StringUtils.isNotEmpty(request.getHeadimgurl())){
            criteria.andHeadimgurlEqualTo(request.getHeadimgurl());
        }
        if(request.getType()!=null){
            criteria.andTypeEqualTo(request.getType());
        }

        if (request.getStartTime() != null) {
            criteria.andCreateTimeGreaterThanOrEqualTo(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            criteria.andCreateTimeLessThanOrEqualTo(request.getEndTime());
        }
    }
}
