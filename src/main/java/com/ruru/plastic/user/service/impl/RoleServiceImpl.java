package com.ruru.plastic.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.dao.RoleMapper;
import com.ruru.plastic.user.enume.RoleLevelEnum;
import com.ruru.plastic.user.enume.StatusEnum;
import com.ruru.plastic.user.model.Role;
import com.ruru.plastic.user.model.RoleExample;
import com.ruru.plastic.user.request.RoleRequest;
import com.ruru.plastic.user.service.RoleService;
import com.ruru.plastic.user.utils.NullUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Role getRoleById(Long id){
        return roleMapper.selectByPrimaryKey(id);
    }

    @Override
    public Role getRoleByName(String name){
        RoleExample example = new RoleExample();
        example.createCriteria().andNameEqualTo(name);
        return roleMapper.selectOneByExample(example);
    }

    @Override
    public Msg<Role> createRole(Role role){
        if(role==null || StringUtils.isEmpty(role.getName())){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        //检查名字有没有重复
        RoleExample example = new RoleExample();
        example.createCriteria().andNameEqualTo(role.getName());
        if(roleMapper.countByExample(example)>0){
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }

        if(role.getLevel()==null){
            role.setLevel(RoleLevelEnum.员工.getValue());
        }
        roleMapper.insertSelective(role);
        return Msg.success(roleMapper.selectByPrimaryKey(role.getId()));
    }


    @Override
    public Msg<Role> updateRole(Role role){
        if(role==null || role.getId()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }

        Role dbRole = roleMapper.selectByPrimaryKey(role.getId());
        if (null==dbRole){
            return Msg.error(Constants.ERROR_NO_INFO);
        }
        BeanUtils.copyProperties(role,dbRole, NullUtil.getNullPropertyNames(role));

        //检查名字有没有重复
        RoleExample example = new RoleExample();
        example.createCriteria().andNameEqualTo(dbRole.getName()).andIdNotEqualTo(dbRole.getId());
        if (roleMapper.countByExample(example) > 0) {
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }

        roleMapper.updateByPrimaryKeySelective(dbRole);

        return Msg.success(roleMapper.selectByPrimaryKey(role.getId()));
    }

    @Override
    public Msg<Role> deleteRole(Role role){
        role.setStatus(StatusEnum.不可用.getValue());
        return updateRole(role);
    }

    @Override
    public List<Role> getRoleList() {
        RoleExample example = new RoleExample();
        example.createCriteria().andStatusEqualTo(StatusEnum.可用.getValue());
        return roleMapper.selectByExample(example);
    }

    @Override
    public PageInfo<Role> filterPagedRoleList(RoleRequest request){
        RoleExample example = new RoleExample();
        RoleExample.Criteria criteria = example.createCriteria();

        if(request.getId()!=null){
            criteria.andIdEqualTo(request.getId());
        }
        if(StringUtils.isNotEmpty(request.getName())){
            criteria.andNameEqualTo(request.getName());
        }
        if(request.getStatus()!=null){
            criteria.andStatusEqualTo(request.getStatus());
        }
        if(request.getLevel()!=null){
            criteria.andLevelEqualTo(request.getLevel());
        }
        if(StringUtils.isNotEmpty(request.getSearch())){
            criteria.andNameLike("%"+request.getSearch()+"%");
        }

        PageHelper.startPage(request.getPage(),request.getSize());
        return new PageInfo<>(roleMapper.selectByExample(example));
    }
}
