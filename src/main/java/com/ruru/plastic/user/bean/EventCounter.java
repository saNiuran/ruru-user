package com.ruru.plastic.user.bean;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EventCounter {
    private Integer enquiryPostCounter;
    private Integer enquiryCheckCounter;
    private Integer enquiryCallCounter;
    private Integer quotationCounter;
    private Integer userCallCounter;
}
