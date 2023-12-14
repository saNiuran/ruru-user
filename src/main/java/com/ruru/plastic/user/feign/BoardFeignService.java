package com.ruru.plastic.user.feign;

import com.ruru.plastic.user.bean.Review;
import com.ruru.plastic.user.bean.ReviewLogRequest;
import com.ruru.plastic.user.response.DataResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(value = "PLASTIC-BOARD-SERVICE")
@Service
public interface BoardFeignService {

    @PostMapping("/review/log/query")
    DataResponse<Integer> queryReviewLog(@RequestBody ReviewLogRequest request);

    @Description("计算某人有回帖且未读的数量")
    @PostMapping("/review/count/unread/reply/user")
    DataResponse<Integer> countUnreadReplyOnReview(@RequestBody Review review);

}
