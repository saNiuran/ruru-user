package com.ruru.plastic.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.dao.UserAccountMapper;
import com.ruru.plastic.user.enume.UserAccountActionTypeEnum;
import com.ruru.plastic.user.model.UserAccount;
import com.ruru.plastic.user.model.UserAccountExample;
import com.ruru.plastic.user.model.UserAccountLog;
import com.ruru.plastic.user.request.UserAccountRequest;
import com.ruru.plastic.user.response.UserAccountResponse;
import com.ruru.plastic.user.service.UserAccountLogService;
import com.ruru.plastic.user.service.UserAccountService;
import com.ruru.plastic.user.service.UserService;
import com.ruru.plastic.user.utils.NullUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private UserAccountMapper userAccountMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserAccountLogService userAccountLogService;

    @Override
    public UserAccount getUserAccountById(Long id){
        return userAccountMapper.selectByPrimaryKey(id);
    }
    @Override
    public UserAccount getUserAccountByUserId(Long userId) {
        UserAccountExample example = new UserAccountExample();
        example.createCriteria().andUserIdEqualTo(userId);
        return userAccountMapper.selectOneByExample(example);
    }

    @Override
    public UserAccountResponse getUserAccountResponseById(Long id){
        UserAccount accountById = getUserAccountById(id);
        if(accountById==null){
            return null;
        }
        UserAccountResponse response = new UserAccountResponse();
        BeanUtils.copyProperties(accountById,response);
        response.setUser(userService.getUserById(accountById.getUserId()));
        return response;
    }

    @Override
    public Msg<UserAccountResponse> createUserAccount(UserAccount userAccount) {
        if (userAccount==null || userAccount.getUserId() == null) {
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        UserAccount dbUserAccount = getUserAccountById(userAccount.getUserId());
        if (dbUserAccount != null) {
            return Msg.success(getUserAccountResponseById(dbUserAccount.getId()));
        }

        if (userAccount.getDeposit() == null) {
            userAccount.setDeposit(0L);
        }
        if(userAccount.getCreateTime()==null){
            userAccount.setCreateTime(new Date());
        }
        if(userAccount.getUpdateTime()==null){
            userAccount.setUpdateTime(new Date());
        }
        userAccountMapper.insertSelective(userAccount);

        //写日志
        userAccountLogService.createUserAccountLog(new UserAccountLog(){{
            setUserAccountId(userAccount.getId());
            setActionType(UserAccountActionTypeEnum.创建.getValue());
            setValue(0L);
        }});

        return Msg.success(getUserAccountResponseById(userAccount.getId()));
    }

    @Override
    public Msg<UserAccountResponse> updateUserAccount(UserAccount userAccount) {
        if(userAccount==null || userAccount.getId()==null){
            return Msg.error(Constants.ERROR_PARAMETER);
        }
        UserAccount dbUserAccount = getUserAccountById(userAccount.getId());
        if(dbUserAccount==null){
            return Msg.error(Constants.ERROR_NO_INFO);
        }

        BeanUtils.copyProperties(userAccount,dbUserAccount, NullUtil.getNullPropertyNames(userAccount));

        UserAccountExample example = new UserAccountExample();
        example.createCriteria().andUserIdEqualTo(dbUserAccount.getUserId()).andIdNotEqualTo(userAccount.getId());
        if (userAccountMapper.countByExample(example) > 0) {
            return Msg.error(Constants.ERROR_DUPLICATE_INFO);
        }

        userAccountMapper.updateByPrimaryKeySelective(dbUserAccount);
        return Msg.success(getUserAccountResponseById(userAccount.getId()));
    }

    @Override
    public PageInfo<UserAccount> filterUserAccount(UserAccountRequest request){
        UserAccountExample example = new UserAccountExample();
        UserAccountExample.Criteria criteria = example.createCriteria();

        if(request.getId()!=null){
            criteria.andIdEqualTo(request.getId());
        }
        if(request.getUserId()!=null){
            criteria.andUserIdEqualTo(request.getUserId());
        }

        PageHelper.startPage(request.getPage(), request.getSize());
        return new PageInfo<>(userAccountMapper.selectByExample(example));
    }

    @Override
    public PageInfo<UserAccountResponse> filterUserAccountResponse(UserAccountRequest request){
        PageInfo<UserAccount> pageInfo = filterUserAccount(request);
        PageInfo<UserAccountResponse> responsePageInfo = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo,responsePageInfo);

        List<UserAccountResponse> responseList = new ArrayList<>();

        for(UserAccount userAccount: pageInfo.getList()){
            responseList.add(getUserAccountResponseById(userAccount.getId()));
        }
        responsePageInfo.setList(responseList);
        return responsePageInfo;
    }

    @Override
    public Msg<UserAccount> increaseDeposit(Long userId, Long depositIncrease) {
        if (depositIncrease < 0) {
            return Msg.error("保证金增加值不能为负数");
        }
        UserAccount userAccount = getUserAccountByUserId(userId);
        userAccount.setDeposit(depositIncrease + userAccount.getDeposit());
        userAccount.setUpdateTime(new Date());
        userAccountMapper.updateByPrimaryKeySelective(userAccount);

        //写日志
        userAccountLogService.createUserAccountLog(new UserAccountLog(){{
            setUserAccountId(userAccount.getId());
            setActionType(UserAccountActionTypeEnum.存入保证金.getValue());
            setValue(depositIncrease);
        }});

        return Msg.success(userAccount);
    }

    @Override
    public Msg<UserAccount> decreaseDeposit(Long userId, Long depositDecrease) {
        if (depositDecrease < 0) {
            return Msg.error("保证金减少值不能为负数");
        }
        UserAccount userAccount = getUserAccountByUserId(userId);
        userAccount.setDeposit(userAccount.getDeposit() - depositDecrease);
        if (userAccount.getDeposit() < 0) {
            return Msg.error("保证金不足扣除！");
        }
        userAccount.setUpdateTime(new Date());
        userAccountMapper.updateByPrimaryKeySelective(userAccount);

        //写日志
        userAccountLogService.createUserAccountLog(new UserAccountLog(){{
            setUserAccountId(userAccount.getId());
            setActionType(UserAccountActionTypeEnum.提取保证金.getValue());
            setValue(depositDecrease);
        }});

        return Msg.success(userAccount);
    }

}
