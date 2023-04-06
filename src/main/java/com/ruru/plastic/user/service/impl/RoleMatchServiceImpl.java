package com.ruru.plastic.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.dao.RoleMatchMapper;
import com.ruru.plastic.user.enume.StatusEnum;
import com.ruru.plastic.user.model.AdminUser;
import com.ruru.plastic.user.model.Role;
import com.ruru.plastic.user.model.RoleMatch;
import com.ruru.plastic.user.model.RoleMatchExample;
import com.ruru.plastic.user.request.RoleMatchRequest;
import com.ruru.plastic.user.response.RoleMatchResponse;
import com.ruru.plastic.user.service.AdminUserService;
import com.ruru.plastic.user.service.RoleMatchService;
import com.ruru.plastic.user.service.RoleService;
import com.ruru.plastic.user.utils.NullUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleMatchServiceImpl implements RoleMatchService {

    @Autowired
    private RoleMatchMapper roleMatchMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AdminUserService adminUserService;

    @Override
    public RoleMatch getRoleMatchById(Long id){
        return roleMatchMapper.selectByPrimaryKey(id);
    }

    @Override
    public RoleMatchResponse getRoleMatchResponseById(Long id){
        RoleMatch roleMatchById = getRoleMatchById(id);
        if(roleMatchById==null){
            return null;
        }
        RoleMatchResponse response = new RoleMatchResponse();
        BeanUtils.copyProperties(roleMatchById,response);
        AdminUser adminUserById = adminUserService.getAdminUserById(roleMatchById.getAdminUserId());
        if(adminUserById!=null){
            adminUserById.setPassword(null);
        }
        response.setAdminUser(adminUserById);
        response.setRole(roleService.getRoleById(roleMatchById.getRoleId()));
        return response;
    }

    @Override
    public List<RoleMatch> getRoleMatchesByAdminUserId(Long adminUserId){
        RoleMatchExample example = new RoleMatchExample();
        example.createCriteria().andAdminUserIdEqualTo(adminUserId);
        return roleMatchMapper.selectByExample(example);
    }

    @Override
    public List<RoleMatch> listRoleMatchByRoleId(Long roleId){
        RoleMatchExample example = new RoleMatchExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        return roleMatchMapper.selectByExample(example);
    }

    @Override
    public List<RoleMatchResponse> getRoleMatchResponsesByAdminUserId(Long adminUserId){
        List<RoleMatch> roleMatchesByAdminUserId = getRoleMatchesByAdminUserId(adminUserId);
        List<RoleMatchResponse> responseList = new ArrayList<>();
        for(RoleMatch match: roleMatchesByAdminUserId){
            Role roleById = roleService.getRoleById(match.getRoleId());
            if(roleById!=null &&roleById.getStatus().equals(StatusEnum.可用.getValue())) {
                responseList.add(getRoleMatchResponseById(match.getId()));
            }
        }
        return responseList;
    }

    @Override
    public Msg<RoleMatch> createRoleMatch(RoleMatch roleMatch){
        if(roleMatch==null || roleMatch.getRoleId()==null || roleMatch.getAdminUserId()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        RoleMatchExample example = new RoleMatchExample();
        example.createCriteria().andRoleIdEqualTo(roleMatch.getRoleId())
                .andAdminUserIdEqualTo(roleMatch.getAdminUserId());
        if(roleMatchMapper.countByExample(example)>0){
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }
        roleMatchMapper.insertSelective(roleMatch);
        return Msg.success(getRoleMatchById(roleMatch.getId()));
    }

    @Override
    public Msg<RoleMatch> updateRoleMatch(RoleMatch roleMatch){
        if(roleMatch==null || roleMatch.getId()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        RoleMatch roleMatchById = getRoleMatchById(roleMatch.getId());
        if(roleMatchById==null){
            return Msg.error(Constants.ERROR_NO_INFO);
        }

        BeanUtils.copyProperties(roleMatch,roleMatchById, NullUtil.getNullPropertyNames(roleMatch));
        RoleMatchExample example = new RoleMatchExample();
        example.createCriteria().andAdminUserIdEqualTo(roleMatchById.getAdminUserId())
                .andRoleIdEqualTo(roleMatchById.getRoleId())
                .andIdNotEqualTo(roleMatchById.getId());
        if(roleMatchMapper.countByExample(example)>0){
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }
        roleMatchMapper.updateByPrimaryKeySelective(roleMatchById);
        return Msg.success(getRoleMatchById(roleMatchById.getId()));
    }

    @Override
    public Msg<Integer> deleteRoleMatch(RoleMatch roleMatch){
        if(roleMatch==null || roleMatch.getId()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        RoleMatch roleMatchById = getRoleMatchById(roleMatch.getId());
        if(roleMatchById==null){
            return Msg.error(Constants.ERROR_NO_INFO);
        }
        roleMatchMapper.deleteByPrimaryKey(roleMatch.getId());
        return Msg.success(1);
    }

    @Override
    public PageInfo<RoleMatch> filterPagedRoleMatchList(RoleMatchRequest request){
        RoleMatchExample example = new RoleMatchExample();
        RoleMatchExample.Criteria criteria = example.createCriteria();
        if(request.getId()!=null){
            criteria.andIdEqualTo(request.getId());
        }
        if(request.getRoleId()!=null){
            criteria.andRoleIdEqualTo(request.getRoleId());
        }
        if(request.getAdminUserId()!=null){
            criteria.andAdminUserIdEqualTo(request.getAdminUserId());
        }

        PageHelper.startPage(request.getPage(),request.getSize());
        return new PageInfo<>(roleMatchMapper.selectByExample(example));
    }

    @Override
    public PageInfo<RoleMatchResponse> filterPagedRoleMatchResponseList(RoleMatchRequest request){
        PageInfo<RoleMatch> pageInfo = filterPagedRoleMatchList(request);
        PageInfo<RoleMatchResponse> responsePageInfo  = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo,responsePageInfo);

        List<RoleMatchResponse> responseList = new ArrayList<>();
        for(RoleMatch roleMatch: pageInfo.getList()){
            responseList.add(getRoleMatchResponseById(roleMatch.getId()));
        }
        responsePageInfo.setList(responseList);
        return responsePageInfo;
    }
}
