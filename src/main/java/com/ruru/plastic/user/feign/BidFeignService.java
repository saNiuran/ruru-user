package com.ruru.plastic.user.feign;

import com.ruru.plastic.user.bean.EnquiryEventLog;
import com.ruru.plastic.user.bean.EventCounter;
import com.ruru.plastic.user.bean.UserCounter;
import com.ruru.plastic.user.model.User;
import com.ruru.plastic.user.response.DataResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(value = "PLASTIC-BID-SERVICE")
@Service
public interface BidFeignService {
    @PostMapping("/event/count")
    DataResponse<UserCounter> countEnquiryAndQuotation(@RequestBody User user);
    @PostMapping("/event/count/day")
    DataResponse<EventCounter> countEnquiryAndQuotationOfToday(@RequestBody User user);

    @PostMapping("/enquiry/event/log/today/action")
    DataResponse<Integer> sameEnquiryEventLogOfToday(@RequestBody EnquiryEventLog enquiryEventLog);
}
