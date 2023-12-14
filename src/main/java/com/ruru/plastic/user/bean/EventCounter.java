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

    private Integer stockPostCounter;
    private Integer stockCheckCounter;
    private Integer stockCallCounter;
    private Integer haggleCounter;
}
