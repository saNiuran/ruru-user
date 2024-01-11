package com.ruru.plastic.user.controller;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.model.UserProperty;
import com.ruru.plastic.user.request.UserPropertyRequest;
import com.ruru.plastic.user.response.DataResponse;
import com.ruru.plastic.user.service.UserPropertyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/property")
public class UserPropertyController {

    @Autowired
    public UserPropertyService userPropertyService;

    @PostMapping("/info")
    public DataResponse<UserProperty> getUserProperty(@RequestBody UserProperty userProperty){
        if(userProperty==null || userProperty.getId()==null){
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        UserProperty userPropertyById = userPropertyService.getUserPropertyById(userProperty.getId());
        if(userPropertyById==null){
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }
        return DataResponse.success(userPropertyById);
    }

    @PostMapping("/new")
    public DataResponse<UserProperty> createUserProperty(@RequestBody UserProperty userProperty){
        Msg<UserProperty> msg = userPropertyService.createUserProperty(userProperty);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }
        return DataResponse.success(msg.getData());
    }

    @PostMapping("/update")
    public DataResponse<UserProperty> updateUserProperty(@RequestBody UserProperty userProperty){
        Msg<UserProperty> msg = userPropertyService.updateUserProperty(userProperty);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }
        return DataResponse.success(msg.getData());
    }

    @PostMapping("/delete")
    public DataResponse<UserProperty> deleteUserProperty(@RequestBody UserProperty userProperty){
        Msg<UserProperty> msg = userPropertyService.deleteUserProperty(userProperty);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }
        return DataResponse.success(msg.getData());
    }

    @PostMapping("/filter")
    public DataResponse<PageInfo<UserProperty>> filterUserProperty(@RequestBody UserPropertyRequest request){
        return DataResponse.success(userPropertyService.filterUserProperty(request));
    }
}
