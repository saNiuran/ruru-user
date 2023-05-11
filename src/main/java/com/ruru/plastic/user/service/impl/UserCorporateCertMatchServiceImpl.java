package com.ruru.plastic.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.dao.UserCorporateCertMatchMapper;
import com.ruru.plastic.user.model.UserCorporateCertMatch;
import com.ruru.plastic.user.model.UserCorporateCertMatchExample;
import com.ruru.plastic.user.request.UserCorporateCertMatchRequest;
import com.ruru.plastic.user.service.UserCorporateCertMatchService;
import com.ruru.plastic.user.utils.NullUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCorporateCertMatchServiceImpl implements UserCorporateCertMatchService {
    @Autowired
    private UserCorporateCertMatchMapper userCorporateCertMatchMapper;

    @Override
    public UserCorporateCertMatch getUserCorporateCertMatchById(Long id){
        return userCorporateCertMatchMapper.selectByPrimaryKey(id);
    }

    @Override
    public UserCorporateCertMatch getUserCorporateCertMatchByUserId(Long userId){
        UserCorporateCertMatchExample example = new UserCorporateCertMatchExample();
        example.createCriteria().andUserIdEqualTo(userId);
        return userCorporateCertMatchMapper.selectOneByExample(example);
    }

    @Override
    public Msg<UserCorporateCertMatch> createUserCorporateCertMatch(UserCorporateCertMatch userCorporateCertMatch){
        if(userCorporateCertMatch==null || userCorporateCertMatch.getUserId()==null || userCorporateCertMatch.getCorporateCertId()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        UserCorporateCertMatchExample example = new UserCorporateCertMatchExample();
        example.createCriteria().andUserIdEqualTo(userCorporateCertMatch.getUserId()).andCorporateCertIdEqualTo(userCorporateCertMatch.getCorporateCertId());
        if(userCorporateCertMatchMapper.countByExample(example)>0){
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }
        userCorporateCertMatch.setId(null);
        userCorporateCertMatchMapper.insertSelective(userCorporateCertMatch);
        return Msg.success(getUserCorporateCertMatchById(userCorporateCertMatch.getId()));
    }

    @Override
    public Msg<UserCorporateCertMatch> updateUserCorporateCertMatch(UserCorporateCertMatch userCorporateCertMatch){
        if(userCorporateCertMatch==null || userCorporateCertMatch.getId()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        UserCorporateCertMatch userCorporateCertMatchById = getUserCorporateCertMatchById(userCorporateCertMatch.getId());
        if(userCorporateCertMatchById==null){
            return Msg.error(Constants.ERROR_NO_INFO);
        }

        BeanUtils.copyProperties(userCorporateCertMatch,userCorporateCertMatchById, NullUtil.getNullPropertyNames(userCorporateCertMatch));
        UserCorporateCertMatchExample example = new UserCorporateCertMatchExample();
        example.createCriteria().andIdNotEqualTo(userCorporateCertMatch.getId()).andUserIdEqualTo(userCorporateCertMatchById.getUserId()).andCorporateCertIdEqualTo(userCorporateCertMatchById.getCorporateCertId());
        if(userCorporateCertMatchMapper.countByExample(example)>0){
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }

        userCorporateCertMatchMapper.updateByPrimaryKeySelective(userCorporateCertMatchById);
        return Msg.success(getUserCorporateCertMatchById(userCorporateCertMatch.getId()));
    }

    @Override
    public Msg<Integer> deleteUserCorporateCertMatch(UserCorporateCertMatch userCorporateCertMatch){
        if(userCorporateCertMatch==null || userCorporateCertMatch.getId()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        UserCorporateCertMatch userCorporateCertMatchById = getUserCorporateCertMatchById(userCorporateCertMatch.getId());
        if(userCorporateCertMatchById==null){
            return Msg.error(Constants.ERROR_NO_INFO);
        }

        userCorporateCertMatchMapper.deleteByPrimaryKey(userCorporateCertMatch.getId());
        return Msg.success(1);
    }

    @Override
    public PageInfo<UserCorporateCertMatch> filterUserCorporateCertMatch(UserCorporateCertMatchRequest request){
        UserCorporateCertMatchExample example = new UserCorporateCertMatchExample();
        UserCorporateCertMatchExample.Criteria criteria = example.createCriteria();

        if(request.getId()!=null){
            criteria.andIdEqualTo(request.getId());
        }
        if(request.getUserId()!=null){
            criteria.andUserIdEqualTo(request.getUserId());
        }
        if(request.getCorporateCertId()!=null){
            criteria.andCorporateCertIdEqualTo(request.getCorporateCertId());
        }

        PageHelper.startPage(request.getPage(),request.getSize());
        return new PageInfo<>(userCorporateCertMatchMapper.selectByExample(example));
    }
}
