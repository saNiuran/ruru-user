package com.ruru.plastic.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.dao.MemberLogMapper;
import com.ruru.plastic.user.model.MemberLog;
import com.ruru.plastic.user.model.MemberLogExample;
import com.ruru.plastic.user.request.MemberLogRequest;
import com.ruru.plastic.user.response.MemberLogResponse;
import com.ruru.plastic.user.service.MemberLogService;
import com.ruru.plastic.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MemberLogServiceImpl implements MemberLogService {

    @Autowired
    private MemberLogMapper memberLogMapper;
    @Autowired
    private UserService userService;

    @Override
    public MemberLog getMemberLogById(Long id) {
        return memberLogMapper.selectByPrimaryKey(id);
    }

    @Override
    public MemberLogResponse getMemberLogResponseById(Long id){
        MemberLog memberLogById = getMemberLogById(id);
        if(memberLogById==null){
            return null;
        }
        MemberLogResponse response = new MemberLogResponse();
        BeanUtils.copyProperties(memberLogById,response);
        response.setUser(userService.getUserById(response.getUserId()));
        return response;
    }

    @Override
    public List<MemberLog> queryMemberLog(MemberLogRequest request) {
        MemberLogExample example = new MemberLogExample();
        MemberLogExample.Criteria criteria = example.createCriteria();

        queryMemberLog(request, criteria);

        example.setOrderByClause("id desc");
        return memberLogMapper.selectByExample(example);
    }

    @Override
    public Msg<MemberLog> createMemberLog(MemberLog log) {
        if(log==null || log.getUserId()==null || log.getActionType()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        log.setId(null);
        if(log.getCreateTime()==null){
            log.setCreateTime(new Date());
        }
        memberLogMapper.insertSelective(log);
        return Msg.success(getMemberLogById(log.getId()));
    }

    @Override
    public PageInfo<MemberLog> filterMemberLog(MemberLogRequest request) {
        MemberLogExample example = new MemberLogExample();
        MemberLogExample.Criteria criteria = example.createCriteria();

        queryMemberLog(request, criteria);

        example.setOrderByClause("id desc");
        PageHelper.startPage(request.getPage(),request.getSize());
        return new PageInfo<>(memberLogMapper.selectByExample(example));
    }

    private static void queryMemberLog(MemberLogRequest request, MemberLogExample.Criteria criteria) {
        if(request.getId()!=null){
            criteria.andIdEqualTo(request.getId());
        }
        if(request.getUserId()!=null){
            criteria.andUserIdEqualTo(request.getUserId());
        }
        if(request.getActionType()!=null){
            criteria.andActionTypeEqualTo(request.getActionType());
        }
        if(request.getDays()!=null){
            criteria.andDaysEqualTo(request.getDays());
        }

        if(request.getStartTime()!=null){
            criteria.andCreateTimeGreaterThanOrEqualTo(request.getStartTime());
        }
        if(request.getEndTime()!=null){
            criteria.andCreateTimeLessThanOrEqualTo(request.getEndTime());
        }
    }
}
