package com.ruru.plastic.user.service;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.model.UserMatch;
import com.ruru.plastic.user.request.UserMatchRequest;

import java.util.List;

public interface UserMatchService {
    UserMatch getUserMatchById(Long id);

    List<UserMatch> queryUserMatch(UserMatchRequest request);

    Msg<UserMatch> createUserMatch(UserMatch userMatch);

    Msg<UserMatch> updateUserMatch(UserMatch userMatch);

    PageInfo<UserMatch> filterUserMatch(UserMatchRequest request);
}
