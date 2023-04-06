package com.ruru.plastic.user.bean;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserCounter {
    private Integer unreadMessageCounter;
    private Integer messageCounter;
    private Integer enquiryCounter;
    private Integer quotationCounter;
    private Integer favoriteEnquiryCounter;
}
