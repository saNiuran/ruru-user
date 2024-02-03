package com.ruru.plastic.user.controller;

import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.model.UserWx;
import com.ruru.plastic.user.request.UserWxRequest;
import com.ruru.plastic.user.response.DataResponse;
import com.ruru.plastic.user.service.UserWxService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/wx")
public class UserWxController {

    @Autowired
    private UserWxService userWxService;

    @PostMapping("/info")
    public DataResponse<UserWx> getUserWxById(@RequestBody UserWx userWx){
        if(userWx==null || userWx.getId()==null){
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        UserWx userWxById = userWxService.getUserWxById(userWx.getId());
        if(userWxById==null){
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }
        return DataResponse.success(userWxById);
    }

    @PostMapping("/query")
    public DataResponse<List<UserWx>> queryUserWx(@RequestBody UserWxRequest request){
        return DataResponse.success(userWxService.queryUserWx(request));
    }

    @PostMapping("/new")
    public DataResponse<UserWx> createUserWx(@RequestBody UserWx userWx){
        Msg<UserWx> msg = userWxService.createUserWx(userWx);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }
        return DataResponse.success(msg.getData());
    }

    @PostMapping("/update")
    public DataResponse<UserWx> updateUserWx(@RequestBody UserWx userWx){
        Msg<UserWx> msg = userWxService.updateUserWx(userWx);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }
        return DataResponse.success(msg.getData());
    }

    @PostMapping("/info/openId")
    public DataResponse<UserWx> getUserWxByOpenId(@RequestBody UserWx userWx){
        if(userWx==null || StringUtils.isEmpty(userWx.getOpenid()) || userWx.getType()==null){
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        List<UserWx> list = userWxService.queryUserWx(new UserWxRequest() {{
            setOpenid(userWx.getOpenid());
            setType(userWx.getType());
        }});

        if(list.size()==0){
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }
        return DataResponse.success(list.get(0));
    }

}
