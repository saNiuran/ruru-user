package com.ruru.plastic.user.service;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.model.UserAccountLog;
import com.ruru.plastic.user.request.UserAccountLogRequest;

public interface UserAccountLogService {

    UserAccountLog getUserAccountLogById(Long id);

    Msg<UserAccountLog> createUserAccountLog(UserAccountLog log);

    PageInfo<UserAccountLog> filterUserAccountLog(UserAccountLogRequest request);
}
