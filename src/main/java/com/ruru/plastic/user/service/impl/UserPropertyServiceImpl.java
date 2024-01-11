package com.ruru.plastic.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.dao.UserPropertyMapper;
import com.ruru.plastic.user.enume.StatusEnum;
import com.ruru.plastic.user.model.UserProperty;
import com.ruru.plastic.user.model.UserPropertyExample;
import com.ruru.plastic.user.request.UserPropertyRequest;
import com.ruru.plastic.user.response.UserPropertyResponse;
import com.ruru.plastic.user.service.UserPropertyService;
import com.ruru.plastic.user.utils.NullUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserPropertyServiceImpl implements UserPropertyService {

    @Autowired
    private UserPropertyMapper userPropertyMapper;

    @Override
    public UserProperty getUserPropertyById(Long id){
        return userPropertyMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<UserProperty> queryUserProperty(UserPropertyRequest request){
        UserPropertyExample example = new UserPropertyExample();
        UserPropertyExample.Criteria criteria = example.createCriteria();

        queryUserProperty(request, criteria);

        example.setOrderByClause("update_time desc");
        return userPropertyMapper.selectByExample(example);
    }

    private static void queryUserProperty(UserProperty userProperty, UserPropertyExample.Criteria criteria) {
        if(userProperty.getId()!=null){
            criteria.andIdEqualTo(userProperty.getId());
        }
        if(userProperty.getUserId()!=null){
            criteria.andUserIdEqualTo(userProperty.getUserId());
        }
        if(userProperty.getPropertyId()!=null){
            criteria.andPropertyIdEqualTo(userProperty.getPropertyId());
        }
        if(userProperty.getStatus()!=null){
            criteria.andStatusEqualTo(userProperty.getStatus());
        }
    }

    @Override
    public Msg<UserProperty> createUserProperty(UserProperty userProperty){
        if(userProperty==null || userProperty.getPropertyId()==null || userProperty.getUserId()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }

        List<UserProperty> list = queryUserProperty(new UserPropertyRequest() {{
            setPropertyId(userProperty.getPropertyId());
            setUserId(userProperty.getUserId());
        }});

        if(list.size()>0){
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }

        userProperty.setId(null);
        if(userProperty.getStatus()==null){
            userProperty.setStatus(StatusEnum.可用.getValue());
        }
        if(userProperty.getCreateTime()==null){
            userProperty.setCreateTime(new Date());
        }
        if(userProperty.getUpdateTime()==null){
            userProperty.setUpdateTime(new Date());
        }

        userPropertyMapper.insertSelective(userProperty);
        return Msg.success(getUserPropertyById(userProperty.getId()));
    }
    @Override
    public Msg<UserProperty> updateUserProperty(UserProperty userProperty){
        if(userProperty==null || userProperty.getId()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        UserProperty userPropertyById = getUserPropertyById(userProperty.getId());
        if(userPropertyById==null){
            return Msg.error(Constants.ERROR_NO_INFO);
        }

        BeanUtils.copyProperties(userProperty,userPropertyById, NullUtil.getNullPropertyNames(userProperty));

        List<UserProperty> list = queryUserProperty(new UserPropertyRequest() {{
            setPropertyId(userPropertyById.getPropertyId());
            setUserId(userPropertyById.getUserId());
        }});

        if(list.stream().anyMatch(v->!v.getId().equals(userProperty.getId()))){
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }

        userPropertyById.setUpdateTime(new Date());
        userPropertyMapper.updateByPrimaryKeySelective(userPropertyById);
        return Msg.success(getUserPropertyById(userProperty.getId()));
    }
    @Override
    public Msg<UserProperty> deleteUserProperty(UserProperty userProperty){
        if(userProperty==null || userProperty.getId()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        userProperty.setStatus(StatusEnum.不可用.getValue());
        return updateUserProperty(userProperty);
    }

    @Override
    public PageInfo<UserProperty> filterUserProperty(UserPropertyRequest request){
        UserPropertyExample example = new UserPropertyExample();
        UserPropertyExample.Criteria criteria = example.createCriteria();

        queryUserProperty(request, criteria);

        example.setOrderByClause("update_time desc");

        PageHelper.startPage(request.getPage(),request.getSize());
        return new PageInfo<>(userPropertyMapper.selectByExample(example));
    }
}
