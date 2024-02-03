package com.ruru.plastic.user.service;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.model.UserChannel;
import com.ruru.plastic.user.request.UserChannelRequest;

import java.util.List;

public interface UserChannelService {

    UserChannel getUserChannel(Long id);

    List<UserChannel> queryUserChannel(UserChannelRequest request);

    Msg<UserChannel> createUserChannel(UserChannel channel);

    Msg<UserChannel> updateUserChannel(UserChannel channel);

    Msg<Integer> deleteUserChannel(UserChannel channel);

    PageInfo<UserChannel> filterUserChannel(UserChannelRequest request);
}
