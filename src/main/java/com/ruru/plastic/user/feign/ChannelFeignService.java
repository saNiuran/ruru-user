package com.ruru.plastic.user.feign;

import com.ruru.plastic.user.model.User;
import com.ruru.plastic.user.response.DataResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("PLASTIC-CHANNEL-SERVICE")
@Service
public interface ChannelFeignService {
    @PostMapping("/potential/mobile/user/register/success")
    DataResponse<Void> registerUserSuccess(@RequestBody User user);
}
