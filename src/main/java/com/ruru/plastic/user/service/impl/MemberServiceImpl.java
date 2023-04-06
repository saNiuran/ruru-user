package com.ruru.plastic.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.dao.MemberMapper;
import com.ruru.plastic.user.enume.StatusEnum;
import com.ruru.plastic.user.enume.TimeTypeEnum;
import com.ruru.plastic.user.model.Member;
import com.ruru.plastic.user.model.MemberExample;
import com.ruru.plastic.user.request.MemberRequest;
import com.ruru.plastic.user.service.MemberService;
import com.ruru.plastic.user.utils.NullUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberMapper memberMapper;

    @Override
    public Member getMemberById(Long id){
        return memberMapper.selectByPrimaryKey(id);
    }

    @Override
    public Member getValidMemberByUserId(Long userId){
        MemberExample example = new MemberExample();
        example.createCriteria().andUserIdEqualTo(userId).andStatusEqualTo(StatusEnum.可用.getValue());
        return memberMapper.selectOneByExample(example);
    }

    @Override
    public Member getMemberByUserId(Long userId){
        MemberExample example = new MemberExample();
        example.createCriteria().andUserIdEqualTo(userId);
        return memberMapper.selectOneByExample(example);
    }

    @Override
    public Msg<Member> createMember(Member member){
        if(member==null || member.getUserId()==null || member.getBeginTime()==null || member.getOverTime()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        Member validMemberByUserId = getValidMemberByUserId(member.getUserId());
        if(validMemberByUserId!=null){
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }

        member.setId(null);
        if(member.getStatus()==null){
            member.setStatus(StatusEnum.可用.getValue());
        }
        if(member.getCreateTime()==null){
            member.setCreateTime(new Date());
        }
        memberMapper.insertSelective(member);
        return Msg.success(getMemberById(member.getId()));
    }

    @Override
    public Msg<Member> updateMember(Member member){
        if(member==null || member.getId()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        Member memberById = getMemberById(member.getId());
        if(memberById==null){
            return Msg.error(Constants.ERROR_NO_INFO);
        }

        BeanUtils.copyProperties(member,memberById, NullUtil.getNullPropertyNames(member));
        MemberExample example = new MemberExample();
        example.createCriteria().andIdNotEqualTo(member.getId()).andUserIdEqualTo(memberById.getUserId()).andStatusEqualTo(StatusEnum.可用.getValue());
        if(memberMapper.countByExample(example)>0){
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }

        memberMapper.updateByPrimaryKeySelective(memberById);
        return Msg.success(getMemberById(member.getId()));
    }

    @Override
    public Msg<Member> deleteMember(Member member){
        if(member==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        member.setStatus(StatusEnum.不可用.getValue());
        return updateMember(member);
    }

    @Override
    public PageInfo<Member> filterMember(MemberRequest request){
        MemberExample example = new MemberExample();
        MemberExample.Criteria criteria = example.createCriteria();

        if(request.getId()!=null){
            criteria.andIdEqualTo(request.getId());
        }
        if(request.getUserId()!=null){
            criteria.andUserIdEqualTo(request.getUserId());
        }
        if(request.getStatus()!=null){
            criteria.andStatusEqualTo(request.getStatus());
        }

        if(request.getStartTime()!=null) {
            switch (TimeTypeEnum.getEnum(request.getTimeType())) {
                case 创建时间:
                    criteria.andCreateTimeGreaterThanOrEqualTo(request.getStartTime());
                    break;
                case 有效时间:
                    criteria.andOverTimeGreaterThanOrEqualTo(request.getStartTime());
                    break;
            }
        }
        if(request.getEndTime()!=null) {
            switch (TimeTypeEnum.getEnum(request.getTimeType())) {
                case 创建时间:
                    criteria.andCreateTimeLessThanOrEqualTo(request.getEndTime());
                    break;
                case 有效时间:
                    criteria.andOverTimeLessThanOrEqualTo(request.getEndTime());
                    break;
            }
        }

        example.setOrderByClause("create_time desc");
        PageHelper.startPage(request.getPage(),request.getSize());
        return new PageInfo<>(memberMapper.selectByExample(example));
    }
}
