package com.ruru.plastic.user.feign;

import com.ruru.plastic.user.bean.Privilege;
import com.ruru.plastic.user.bean.SystemConfig;
import com.ruru.plastic.user.response.DataResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient(value = "PLASTIC-CONFIG-SERVICE")
@Service
public interface ConfigFeignService {

    @PostMapping("/privilege/query")
    DataResponse<List<Privilege>> queryPrivilege(@RequestBody Privilege privilege);

    @PostMapping("/system/config/info/name")
    DataResponse<SystemConfig> getSystemConfigByName(@RequestBody SystemConfig systemConfig);
}
