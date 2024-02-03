package com.ruru.plastic.user.feign;

import com.ruru.plastic.user.bean.Message;
import com.ruru.plastic.user.bean.PushBody;
import com.ruru.plastic.user.bean.UserCounter;
import com.ruru.plastic.user.bean.WxSafe;
import com.ruru.plastic.user.model.User;
import com.ruru.plastic.user.response.DataResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(value = "PLASTIC-SMS-SERVICE")
@Service
public interface SmsFeignService {

    @PostMapping("/message/new")
    DataResponse<Message> createMessage(@RequestBody Message message);

    @PostMapping("/message/count")
    DataResponse<UserCounter> countMessage(@RequestBody User user);

    @PostMapping("/push/bean/pool/new")
    DataResponse<Void> createPush(@RequestBody PushBody pushBody);

    @PostMapping("/wx/safe/check")
    DataResponse<Boolean> check(@RequestBody WxSafe wxSafe);
}
