package com.ruru.plastic.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.dao.UserMatchMapper;
import com.ruru.plastic.user.model.UserMatch;
import com.ruru.plastic.user.model.UserMatchExample;
import com.ruru.plastic.user.request.UserMatchRequest;
import com.ruru.plastic.user.service.UserMatchService;
import com.ruru.plastic.user.utils.NullUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserMatchServiceImpl implements UserMatchService {

    @Autowired
    private UserMatchMapper userMatchMapper;

    @Override
    public UserMatch getUserMatchById(Long id){
        return userMatchMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<UserMatch> queryUserMatch(UserMatchRequest request){
        UserMatchExample example  =new UserMatchExample();
        UserMatchExample.Criteria criteria = example.createCriteria();

        queryUserMatch(request, criteria);

        return userMatchMapper.selectByExample(example);
    }

    @Override
    public Msg<UserMatch> createUserMatch(UserMatch userMatch){
        if(userMatch==null || userMatch.getUserId()==null || userMatch.getTargetType()==null || userMatch.getTargetId()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }

        List<UserMatch> list = queryUserMatch(new UserMatchRequest() {{
            setUserId(userMatch.getUserId());
            setTargetType(userMatch.getTargetType());
            setTargetId(userMatch.getTargetId());
        }});

        if(list.size()>0){
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }

        userMatch.setId(null);
        if(userMatch.getCreateTime()==null){
            userMatch.setCreateTime(new Date());
        }

        userMatchMapper.insertSelective(userMatch);

        return Msg.success(getUserMatchById(userMatch.getId()));
    }

    @Override
    public Msg<UserMatch> updateUserMatch(UserMatch userMatch){
        if(userMatch==null || userMatch.getId()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        UserMatch userMatchById = getUserMatchById(userMatch.getId());
        if(userMatchById==null){
            return Msg.error(Constants.ERROR_NO_INFO);
        }

        BeanUtils.copyProperties(userMatch,userMatchById, NullUtil.getNullPropertyNames(userMatch));
        List<UserMatch> list = queryUserMatch(new UserMatchRequest() {{
            setUserId(userMatchById.getUserId());
            setTargetType(userMatchById.getTargetType());
            setTargetId(userMatchById.getTargetId());
        }});

        if(list.stream().anyMatch(v->!v.getId().equals(userMatch.getId()))){
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }

        userMatchMapper.updateByPrimaryKeySelective(userMatchById);
        return Msg.success(getUserMatchById(userMatch.getId()));
    }

    @Override
    public PageInfo<UserMatch> filterUserMatch(UserMatchRequest request){
        UserMatchExample example  =new UserMatchExample();
        UserMatchExample.Criteria criteria = example.createCriteria();

        queryUserMatch(request, criteria);

        PageHelper.startPage(request.getPage(),request.getSize());

        example.setOrderByClause("create_time desc");

        return new PageInfo<>(userMatchMapper.selectByExample(example));
    }

    private static void queryUserMatch(UserMatchRequest request, UserMatchExample.Criteria criteria) {
        if(request.getId()!=null){
            criteria.andIdEqualTo(request.getId());
        }
        if(request.getUserId()!=null){
            criteria.andUserIdEqualTo(request.getUserId());
        }
        if(request.getTargetId()!=null){
            criteria.andTargetIdEqualTo(request.getTargetId());
        }
        if(request.getTargetType()!=null){
            criteria.andTargetTypeEqualTo(request.getTargetType());
        }
        if(request.getStartTime()!=null){
            criteria.andCreateTimeGreaterThanOrEqualTo(request.getStartTime());
        }
        if(request.getEndTime()!=null){
            criteria.andCreateTimeLessThanOrEqualTo(request.getEndTime());
        }
    }
}
