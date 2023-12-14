package com.ruru.plastic.user.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class ReviewLogRequest extends ReviewLog implements Serializable {
    private Integer page = 1;
    private Integer size = 10;
    private String orderClause;
    private Date startTime;
    private Date endTime;
}
