package com.ruru.plastic.user.bean;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserCounter {
    private Integer enquiryCounter;
    private Integer quotationCounter;
    private Integer favoriteEnquiryCounter;
    private Integer stockCounter;
    private Integer haggleCounter;
    private Integer favoriteStockCounter;

    private Integer messageCounter;
    private Integer unreadMessageCounter;

    private Integer shareCounter;
    private Integer reviewCounter;
    private Integer unreadReplyCounter;
    private Integer callCounter;
}
