package com.ruru.plastic.user.controller;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.MembershipOrder;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.bean.SystemConfig;
import com.ruru.plastic.user.enume.MemberActionTypeEnum;
import com.ruru.plastic.user.enume.UserMemberLevelEnum;
import com.ruru.plastic.user.feign.ConfigFeignService;
import com.ruru.plastic.user.model.Member;
import com.ruru.plastic.user.model.MemberLog;
import com.ruru.plastic.user.model.User;
import com.ruru.plastic.user.net.LoginRequired;
import com.ruru.plastic.user.request.MemberLogRequest;
import com.ruru.plastic.user.request.MemberRequest;
import com.ruru.plastic.user.response.DataResponse;
import com.ruru.plastic.user.response.MemberLogResponse;
import com.ruru.plastic.user.service.MemberLogService;
import com.ruru.plastic.user.service.MemberService;
import com.ruru.plastic.user.service.UserService;
import com.xiaoleilu.hutool.date.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberLogService memberLogService;
    @Autowired
    private UserService userService;
    @Autowired
    private ConfigFeignService configFeignService;

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

    @PostMapping("/extend")
    public DataResponse<Member> extendMember(@RequestBody MembershipOrder membershipOrder){
        if(membershipOrder==null || membershipOrder.getId()==null || membershipOrder.getDay()==null || membershipOrder.getUserId()==null){
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }

        boolean promote= false;
        DataResponse<SystemConfig> dataResponse = configFeignService.getSystemConfigByName(new SystemConfig() {{
            setName("member_pay_promote_one_year");
        }});
        if(dataResponse.getRetCode()==0 && dataResponse.getData().getValue().equals("1")){
            promote = true;
        }

        Member validMemberByUserId = memberService.getValidMemberByUserId(membershipOrder.getUserId());
        Msg<Member> msg;
        if(validMemberByUserId==null){
            boolean finalPromote = promote;
            msg = memberService.createMember(new Member() {{
                setUserId(membershipOrder.getUserId());
                setBeginTime(new Date());
                if(membershipOrder.getDay()==365 && finalPromote){
                    setOverTime(DateUtil.offsiteDay(new Date(), membershipOrder.getDay()+365));
                }else{
                    setOverTime(DateUtil.offsiteDay(new Date(), membershipOrder.getDay()));
                }
            }});
        }else{
            if(membershipOrder.getDay()==365 && promote) {
                validMemberByUserId.setOverTime(DateUtil.offsiteDay(validMemberByUserId.getOverTime(), membershipOrder.getDay()+365));
            }else{
                validMemberByUserId.setOverTime(DateUtil.offsiteDay(validMemberByUserId.getOverTime(), membershipOrder.getDay()));
            }
            msg = memberService.updateMember(validMemberByUserId);
        }
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }

        User userById = userService.getUserById(membershipOrder.getUserId());
        if(userById!=null){
            userById.setMemberLevel(UserMemberLevelEnum.付费用户.getValue());
            userService.updateUser(userById);
        }

        List<MemberLog> memberLogList = memberLogService.queryMemberLog(new MemberLogRequest() {{
            setUserId(msg.getData().getUserId());
            setActionType(MemberActionTypeEnum.创建.getValue());
        }});

        memberLogService.createMemberLog(new MemberLog(){{
            setUserId(msg.getData().getUserId());
            setActionType(memberLogList.size()>0?MemberActionTypeEnum.展期.getValue():MemberActionTypeEnum.创建.getValue());
            setDays(membershipOrder.getDay());
            setRemark(memberLogList.size()>0?"会员续费":"会员年费");
        }});

        return DataResponse.success(msg.getData());
    }

    @PostMapping("/log/query")
    public DataResponse<List<MemberLog>> queryMemberLog(@RequestBody MemberLogRequest request){
        return DataResponse.success(memberLogService.queryMemberLog(request));
    }

    @PostMapping("/log/last/two")
    public DataResponse<List<MemberLogResponse>> getLastTwoMemberLog(){
        List<MemberLog> logList = memberLogService.queryMemberLog(new MemberLogRequest() {{
            setStartTime(DateUtil.offsiteDay(new Date(), -3));  //过去三天
            setActionType(MemberActionTypeEnum.展期.getValue());
        }});

        List<MemberLog> subList = logList.size()>2?logList.subList(0, 2):logList;

        List<MemberLogResponse> responseList = new ArrayList<>();

        for(MemberLog log: subList){
            responseList.add(memberLogService.getMemberLogResponseById(log.getId()));
        }

        return DataResponse.success(responseList);
    }
}
