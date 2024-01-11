package com.ruru.plastic.user.service;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.model.UserCorporateCertMatch;
import com.ruru.plastic.user.request.UserCorporateCertMatchRequest;
import com.ruru.plastic.user.response.UserCorporateCertMatchResponse;

import java.util.List;

public interface UserCorporateCertMatchService {
    UserCorporateCertMatch getUserCorporateCertMatchById(Long id);

    UserCorporateCertMatch getUserCorporateCertMatchByUserId(Long userId);

    List<UserCorporateCertMatch> queryUserCorporateCertMatch(UserCorporateCertMatchRequest request);

    Msg<UserCorporateCertMatch> createUserCorporateCertMatch(UserCorporateCertMatch userCorporateCertMatch);

    Msg<UserCorporateCertMatch> updateUserCorporateCertMatch(UserCorporateCertMatch userCorporateCertMatch);

    Msg<Integer> deleteUserCorporateCertMatch(UserCorporateCertMatch userCorporateCertMatch);

    PageInfo<UserCorporateCertMatch> filterUserCorporateCertMatch(UserCorporateCertMatchRequest request);

}
