package com.ruru.plastic.user.controller;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.model.User;
import com.ruru.plastic.user.model.UserAccount;
import com.ruru.plastic.user.model.UserAccountLog;
import com.ruru.plastic.user.net.CurrentUser;
import com.ruru.plastic.user.net.LoginRequired;
import com.ruru.plastic.user.request.UserAccountLogRequest;
import com.ruru.plastic.user.response.DataResponse;
import com.ruru.plastic.user.service.UserAccountLogService;
import com.ruru.plastic.user.service.UserAccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/account")
public class UserAccountController {
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserAccountLogService userAccountLogService;

    @LoginRequired
    @PostMapping("/info/my")
    public DataResponse<UserAccount> getMyUserAccount(@CurrentUser User user){
        UserAccount userAccountByUserId = userAccountService.getUserAccountByUserId(user.getId());
        if(userAccountByUserId==null){
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }
        return DataResponse.success(userAccountByUserId);
    }

    @PostMapping("/deposit/increase")
    public DataResponse<UserAccount> increaseDeposit(@RequestBody UserAccount userAccount){
        Msg<UserAccount> msg = userAccountService.increaseDeposit(userAccount.getUserId(), userAccount.getDeposit());
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }
        return DataResponse.success(msg.getData());
    }

    @PostMapping("/deposit/decrease")
    public DataResponse<UserAccount> decreaseDeposit(@RequestBody UserAccount userAccount){
        Msg<UserAccount> msg = userAccountService.decreaseDeposit(userAccount.getUserId(), userAccount.getDeposit());
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }
        return DataResponse.success(msg.getData());
    }

    @LoginRequired
    @PostMapping("/log/filter")
    public DataResponse<PageInfo<UserAccountLog>> filterUserAccountLog(@RequestBody UserAccountLogRequest request){
        return DataResponse.success(userAccountLogService.filterUserAccountLog(request));
    }
}
