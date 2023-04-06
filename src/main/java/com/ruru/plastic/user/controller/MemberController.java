package com.ruru.plastic.user.controller;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.MembershipOrder;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.enume.MemberActionTypeEnum;
import com.ruru.plastic.user.model.Member;
import com.ruru.plastic.user.model.MemberLog;
import com.ruru.plastic.user.model.User;
import com.ruru.plastic.user.net.CurrentUser;
import com.ruru.plastic.user.net.LoginRequired;
import com.ruru.plastic.user.request.MemberRequest;
import com.ruru.plastic.user.response.DataResponse;
import com.ruru.plastic.user.service.MemberLogService;
import com.ruru.plastic.user.service.MemberService;
import com.xiaoleilu.hutool.date.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberLogService memberLogService;

    @LoginRequired
    @PostMapping("/info")
    public DataResponse<Member> getMemberById(@RequestBody Member member){
        if(member==null || member.getId()==null){
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        Member memberById = memberService.getMemberById(member.getId());
        if(memberById==null){
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }
        return DataResponse.success(memberById);
    }

    @LoginRequired
    @PostMapping("/new")
    public DataResponse<Member> createMember(@RequestBody MemberRequest request){
        if(request.getDay()==null){
            request.setDay(0);
        }

        Msg<Member> msg = memberService.createMember(request);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }

        memberLogService.createMemberLog(new MemberLog(){{
            setUserId(msg.getData().getUserId());
            setActionType(MemberActionTypeEnum.创建.getValue());
            setDays(request.getDay());
            setRemark("新建会员");
        }});

        return DataResponse.success(msg.getData());
    }

    @LoginRequired
    @PostMapping("/delete")
    public DataResponse<Member> deleteMember(@RequestBody Member member){
        Msg<Member> msg = memberService.deleteMember(member);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }

        memberLogService.createMemberLog(new MemberLog(){{
            setUserId(msg.getData().getUserId());
            setActionType(MemberActionTypeEnum.过期.getValue());
            setRemark("会员过期");
        }});

        return DataResponse.success(msg.getData());
    }

    @LoginRequired
    @PostMapping("/filter")
    public DataResponse<PageInfo<Member>> filterMember(@RequestBody MemberRequest request){
        return DataResponse.success(memberService.filterMember(request));
    }

    @LoginRequired
    @PostMapping("/extend")
    public DataResponse<Member> extendMember(@RequestBody MembershipOrder membershipOrder, @CurrentUser User user){
        if(membershipOrder==null || membershipOrder.getDay()==null || membershipOrder.getUserId()==null || !membershipOrder.getUserId().equals(user.getId())){
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }

        Member validMemberByUserId = memberService.getValidMemberByUserId(user.getId());
        Msg<Member> msg;
        if(validMemberByUserId==null){
            msg = memberService.createMember(new Member() {{
                setUserId(user.getId());
                setBeginTime(new Date());
                setOverTime(DateUtil.offsiteDay(new Date(), membershipOrder.getDay()));
            }});
        }else{
            validMemberByUserId.setOverTime(DateUtil.offsiteDay(validMemberByUserId.getOverTime(),membershipOrder.getDay()));
            msg = memberService.updateMember(validMemberByUserId);
        }
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }

        memberLogService.createMemberLog(new MemberLog(){{
            setUserId(msg.getData().getUserId());
            setActionType(MemberActionTypeEnum.展期.getValue());
            setDays(membershipOrder.getDay());
            setRemark("会员续费");
        }});

        return DataResponse.success(msg.getData());
    }
}
