package com.ruru.plastic.user.service;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.model.UserCorporateCertMatch;
import com.ruru.plastic.user.request.UserCorporateCertMatchRequest;

public interface UserCorporateCertMatchService {
    UserCorporateCertMatch getUserCorporateCertMatchById(Long id);

    UserCorporateCertMatch getUserCorporateCertMatchByUserId(Long userId);

    Msg<UserCorporateCertMatch> createUserCorporateCertMatch(UserCorporateCertMatch userCorporateCertMatch);

    Msg<UserCorporateCertMatch> updateUserCorporateCertMatch(UserCorporateCertMatch userCorporateCertMatch);

    Msg<Integer> deleteUserCorporateCertMatch(UserCorporateCertMatch userCorporateCertMatch);

    PageInfo<UserCorporateCertMatch> filterUserCorporateCertMatch(UserCorporateCertMatchRequest request);
}
