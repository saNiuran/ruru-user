package com.ruru.plastic.user.service;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.model.MemberLog;
import com.ruru.plastic.user.request.MemberLogRequest;

import java.util.List;

public interface MemberLogService {
    MemberLog getMemberLogById(Long id);

    List<MemberLog> queryMemberLog(MemberLogRequest request);

    Msg<MemberLog> createMemberLog(MemberLog log);

    PageInfo<MemberLog> filterMemberLog(MemberLogRequest request);
}
