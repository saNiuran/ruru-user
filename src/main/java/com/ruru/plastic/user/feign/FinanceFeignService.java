package com.ruru.plastic.user.feign;

import com.ruru.plastic.user.bean.DepositConfig;
import com.ruru.plastic.user.response.DataResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@FeignClient(value = "PLASTIC-FINANCE-SERVICE")
@Service
public interface FinanceFeignService {

    @PostMapping("/deposit/config/list/valid")
    DataResponse<List<DepositConfig>> listValidDepositConfig();
}
