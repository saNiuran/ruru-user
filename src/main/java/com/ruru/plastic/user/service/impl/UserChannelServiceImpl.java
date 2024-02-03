package com.ruru.plastic.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.dao.UserChannelMapper;
import com.ruru.plastic.user.model.UserChannel;
import com.ruru.plastic.user.model.UserChannelExample;
import com.ruru.plastic.user.request.UserChannelRequest;
import com.ruru.plastic.user.service.UserChannelService;
import com.ruru.plastic.user.utils.NullUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserChannelServiceImpl implements UserChannelService {

    @Autowired
    private UserChannelMapper userChannelMapper;

    @Override
    public UserChannel getUserChannel(Long id){
        return userChannelMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<UserChannel> queryUserChannel(UserChannelRequest request){
        UserChannelExample example = new UserChannelExample();
        UserChannelExample.Criteria criteria = example.createCriteria();

        queryUserChannel(request, criteria);

        example.setOrderByClause("create_time desc");
        return userChannelMapper.selectByExample(example);
    }

    private static void queryUserChannel(UserChannelRequest request, UserChannelExample.Criteria criteria) {
        if(request.getId()!=null){
            criteria.andIdEqualTo(request.getId());
        }
        if(request.getUserId()!=null){
            criteria.andUserIdEqualTo(request.getUserId());
        }
        if(request.getAgentUserId()!=null){
            criteria.andAgentUserIdEqualTo(request.getAgentUserId());
        }
    }

    @Override
    public Msg<UserChannel> createUserChannel(UserChannel channel){
        if(channel==null || channel.getUserId()==null || channel.getAgentUserId()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        List<UserChannel> list = queryUserChannel(new UserChannelRequest() {{
            setUserId(channel.getUserId());
            setAgentUserId(channel.getAgentUserId());
        }});

        if(list.size()>0){
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }

        channel.setId(null);
        if(channel.getCreateTime()==null){
            channel.setCreateTime(new Date());
        }
        userChannelMapper.insertSelective(channel);
        return Msg.success(getUserChannel(channel.getId()));
    }

    @Override
    public Msg<UserChannel> updateUserChannel(UserChannel channel){
        if(channel==null || channel.getId()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        UserChannel userChannel = getUserChannel(channel.getId());
        if(userChannel==null){
            return Msg.error(Constants.ERROR_NO_INFO);
        }

        BeanUtils.copyProperties(channel,userChannel, NullUtil.getNullPropertyNames(channel));

        List<UserChannel> list = queryUserChannel(new UserChannelRequest() {{
            setUserId(userChannel.getUserId());
            setAgentUserId(userChannel.getAgentUserId());
        }});

        if(list.stream().anyMatch(v->!v.getId().equals(channel.getId()))){
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }

        userChannelMapper.updateByPrimaryKeySelective(userChannel);
        return Msg.success(getUserChannel(channel.getId()));
    }

    @Override
    public Msg<Integer> deleteUserChannel(UserChannel channel){
        if(channel==null || channel.getId()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        UserChannel userChannel = getUserChannel(channel.getId());
        if(userChannel==null){
            return Msg.error(Constants.ERROR_NO_INFO);
        }

        userChannelMapper.deleteByPrimaryKey(channel.getId());
        return Msg.success(1);
    }

    @Override
    public PageInfo<UserChannel> filterUserChannel(UserChannelRequest request){
        UserChannelExample example = new UserChannelExample();
        UserChannelExample.Criteria criteria = example.createCriteria();

        queryUserChannel(request, criteria);

        if(request.getStartTime()!=null){
            criteria.andCreateTimeGreaterThanOrEqualTo(request.getStartTime());
        }
        if(request.getEndTime()!=null){
            criteria.andCreateTimeLessThanOrEqualTo(request.getEndTime());
        }

        example.setOrderByClause("create_time desc");
        PageHelper.startPage(request.getPage(),request.getSize());

        return new PageInfo<>(userChannelMapper.selectByExample(example));
    }
}
