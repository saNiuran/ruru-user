package com.ruru.plastic.user.service;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.model.UserAccount;
import com.ruru.plastic.user.request.UserAccountRequest;
import com.ruru.plastic.user.response.UserAccountResponse;

public interface UserAccountService {

    UserAccount getUserAccountById(Long id);

    UserAccount getUserAccountByUserId(Long userId);

    UserAccountResponse getUserAccountResponseById(Long id);

    Msg<UserAccountResponse> createUserAccount(UserAccount userAccount);

    Msg<UserAccountResponse> updateUserAccount(UserAccount userAccount);

    PageInfo<UserAccount> filterUserAccount(UserAccountRequest request);

    PageInfo<UserAccountResponse> filterUserAccountResponse(UserAccountRequest request);

    Msg<UserAccount> increaseDeposit(Long userId, Long depositIncrease);

    Msg<UserAccount> decreaseDeposit(Long userId, Long depositDecrease);
}
