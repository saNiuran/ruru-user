package com.ruru.plastic.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.dao.UserAccountLogMapper;
import com.ruru.plastic.user.model.UserAccountLog;
import com.ruru.plastic.user.model.UserAccountLogExample;
import com.ruru.plastic.user.request.UserAccountLogRequest;
import com.ruru.plastic.user.service.UserAccountLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserAccountLogServiceImpl implements UserAccountLogService {

    @Autowired
    private UserAccountLogMapper userAccountLogMapper;

    @Override
    public UserAccountLog getUserAccountLogById(Long id) {
        return userAccountLogMapper.selectByPrimaryKey(id);
    }

    @Override
    public Msg<UserAccountLog> createUserAccountLog(UserAccountLog log) {
        if (log == null || log.getActionType()==null || log.getUserAccountId() == null || log.getValue() == null) {
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        log.setId(null);
        if(log.getCreateTime()==null) {
            log.setCreateTime(new Date());
        }
        userAccountLogMapper.insertSelective(log);
        return Msg.success(getUserAccountLogById(log.getId()));
    }

    @Override
    public PageInfo<UserAccountLog> filterUserAccountLog(UserAccountLogRequest request){
        UserAccountLogExample example = new UserAccountLogExample();
        UserAccountLogExample.Criteria criteria = example.createCriteria();

        if(request.getId()!=null){
            criteria.andIdEqualTo(request.getId());
        }
        if(request.getUserAccountId()!=null){
            criteria.andUserAccountIdEqualTo(request.getUserAccountId());
        }
        if(StringUtils.isNotEmpty(request.getRemark())){
            criteria.andRemarkEqualTo(request.getRemark());
        }
        if(request.getActionType()!=null){
            criteria.andActionTypeEqualTo(request.getActionType());
        }
        if(request.getValue()!=null){
            criteria.andValueEqualTo(request.getValue());
        }

        if(request.getStartTime()!=null){
            criteria.andCreateTimeGreaterThanOrEqualTo(request.getStartTime());
        }
        if(request.getEndTime()!=null){
            criteria.andCreateTimeLessThanOrEqualTo(request.getEndTime());
        }

        PageHelper.startPage(request.getPage(),request.getSize());

        return new PageInfo<>(userAccountLogMapper.selectByExample(example));
    }

}
