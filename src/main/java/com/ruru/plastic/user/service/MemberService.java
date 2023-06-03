package com.ruru.plastic.user.service;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.model.Member;
import com.ruru.plastic.user.request.MemberRequest;

import java.util.List;

public interface MemberService {
    Member getMemberById(Long id);

    Member getValidMemberByUserId(Long userId);

    Member getMemberByUserId(Long userId);

    Msg<Member> createMember(Member member);

    Msg<Member> updateMember(Member member);

    Msg<Member> deleteMember(Member member);

    PageInfo<Member> filterMember(MemberRequest request);

    List<Member> queryMemberOverdue();

    List<Member> queryMember(Member member);
}
