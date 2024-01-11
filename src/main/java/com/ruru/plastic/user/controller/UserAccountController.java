package com.ruru.plastic.user.controller;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.enume.StatusEnum;
import com.ruru.plastic.user.model.*;
import com.ruru.plastic.user.net.CurrentUser;
import com.ruru.plastic.user.net.LoginRequired;
import com.ruru.plastic.user.request.UserAccountLogRequest;
import com.ruru.plastic.user.request.UserPropertyRequest;
import com.ruru.plastic.user.response.DataResponse;
import com.ruru.plastic.user.service.PropertyService;
import com.ruru.plastic.user.service.UserAccountLogService;
import com.ruru.plastic.user.service.UserAccountService;
import com.ruru.plastic.user.service.UserPropertyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ruru.plastic.user.bean.Constants.SINGLE_PRODUCT_EXPERT;

@RestController
@RequestMapping("/user/account")
public class UserAccountController {
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserAccountLogService userAccountLogService;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private UserPropertyService userPropertyService;

    @LoginRequired
    @PostMapping("/info/my")
    public DataResponse<UserAccount> getMyUserAccount(@CurrentUser User user) {
        UserAccount userAccountByUserId = userAccountService.getUserAccountByUserId(user.getId());
        if (userAccountByUserId == null) {
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }
        return DataResponse.success(userAccountByUserId);
    }

    @PostMapping("/deposit/increase")
    public DataResponse<UserAccount> increaseDeposit(@RequestBody UserAccount userAccount) {
        Msg<UserAccount> msg = userAccountService.increaseDeposit(userAccount.getUserId(), userAccount.getDeposit());
        if (StringUtils.isNotEmpty(msg.getErrorMsg())) {
            return DataResponse.error(msg.getErrorMsg());
        }

        //给用户赠送单品专家
        List<Property> list = propertyService.queryProperty(new Property() {{
            setSign(SINGLE_PRODUCT_EXPERT);     //查单品专家的编号
            setStatus(StatusEnum.可用.getValue());
        }});
        if (list.size() > 0) {
            if (list.get(0).getStatus().equals(StatusEnum.可用.getValue())) {
                List<UserProperty> list1 = userPropertyService.queryUserProperty(new UserPropertyRequest() {{
                    setPropertyId(list.get(0).getId());
                    setUserId(userAccount.getUserId());
                }});

                if (list1.size() == 0) {
                    userPropertyService.createUserProperty(new UserProperty() {{
                        setUserId(userAccount.getUserId());
                        setPropertyId(list.get(0).getId());
                    }});
                } else {
                    if (list1.get(0).getStatus().equals(StatusEnum.不可用.getValue())) {
                        list1.get(0).setStatus(StatusEnum.可用.getValue());
                        userPropertyService.updateUserProperty(list1.get(0));
                    }
                }
            }
        }

        return DataResponse.success(msg.getData());
    }

    @PostMapping("/deposit/decrease")
    public DataResponse<UserAccount> decreaseDeposit(@RequestBody UserAccount userAccount) {
        Msg<UserAccount> msg = userAccountService.decreaseDeposit(userAccount.getUserId(), userAccount.getDeposit());
        if (StringUtils.isNotEmpty(msg.getErrorMsg())) {
            return DataResponse.error(msg.getErrorMsg());
        }

        if (msg.getData().getDeposit().equals(0L)) {

            //取消单品专家
            List<Property> list = propertyService.queryProperty(new Property() {{
                setSign(SINGLE_PRODUCT_EXPERT);     //查单品专家的编号
            }});
            if (list.size() > 0) {
                List<UserProperty> list1 = userPropertyService.queryUserProperty(new UserPropertyRequest() {{
                    setPropertyId(list.get(0).getId());
                    setUserId(userAccount.getUserId());
                    setStatus(StatusEnum.可用.getValue());
                }});

                if (list1.size() > 0) {
                    list1.get(0).setStatus(StatusEnum.不可用.getValue());
                    userPropertyService.updateUserProperty(list1.get(0));
                }
            }
        }

        return DataResponse.success(msg.getData());
    }

    @LoginRequired
    @PostMapping("/log/filter")
    public DataResponse<PageInfo<UserAccountLog>> filterUserAccountLog(@RequestBody UserAccountLogRequest request) {
        return DataResponse.success(userAccountLogService.filterUserAccountLog(request));
    }
}
