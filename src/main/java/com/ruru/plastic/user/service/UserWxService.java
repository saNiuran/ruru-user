package com.ruru.plastic.user.service;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.model.UserWx;
import com.ruru.plastic.user.request.UserWxRequest;

import java.util.List;

public interface UserWxService {
    UserWx getUserWxById(Long id);

    UserWx getUserWxByOpenId(String openId);

    List<UserWx> queryUserWx(UserWxRequest request);

    Msg<UserWx> createUserWx(UserWx userWx);

    Msg<UserWx> updateUserWx(UserWx userWx);

    Msg<Integer> deleteUserWx(UserWx userWx);

    PageInfo<UserWx> filterUserWx(UserWxRequest request);
}
