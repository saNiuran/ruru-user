package com.ruru.plastic.user.service;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.model.RoleMatch;
import com.ruru.plastic.user.request.RoleMatchRequest;
import com.ruru.plastic.user.response.RoleMatchResponse;

import java.util.List;
public interface RoleMatchService {
    RoleMatch getRoleMatchById(Long id);

    RoleMatchResponse getRoleMatchResponseById(Long id);

    List<RoleMatch> getRoleMatchesByAdminUserId(Long adminUserId);

    List<RoleMatch> listRoleMatchByRoleId(Long roleId);

    List<RoleMatchResponse> getRoleMatchResponsesByAdminUserId(Long adminUserId);

    Msg<RoleMatch> createRoleMatch(RoleMatch roleMatch);

    Msg<RoleMatch> updateRoleMatch(RoleMatch roleMatch);

    Msg<Integer> deleteRoleMatch(RoleMatch roleMatch);

    PageInfo<RoleMatch> filterPagedRoleMatchList(RoleMatchRequest request);

    PageInfo<RoleMatchResponse> filterPagedRoleMatchResponseList(RoleMatchRequest request);
}
