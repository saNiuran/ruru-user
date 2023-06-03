package com.ruru.plastic.user.task;

import com.ruru.plastic.user.enume.StatusEnum;
import com.ruru.plastic.user.enume.UserMemberLevelEnum;
import com.ruru.plastic.user.model.Member;
import com.ruru.plastic.user.model.User;
import com.ruru.plastic.user.service.MemberService;
import com.ruru.plastic.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduleTask {
    @Autowired
    private MemberService memberService;
    @Autowired
    private UserService userService;

    @Scheduled(cron="0 30 0 * * *")
    public void overdueUserMember(){
        List<Member> memberList = memberService.queryMemberOverdue();

        for(Member member: memberList){
            User userById = userService.getUserById(member.getUserId());
            if(userById!=null){
                userById.setMemberLevel(UserMemberLevelEnum.普通用户.getValue());
                userService.updateUser(userById);
            }

            member.setStatus(StatusEnum.不可用.getValue());
            memberService.updateMember(member);
        }
    }
}
