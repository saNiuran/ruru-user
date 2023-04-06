package com.ruru.plastic.user.request;

import com.ruru.plastic.user.model.Member;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class MemberRequest extends Member implements Serializable {
    private Integer page = 1;
    private Integer size = 10;
    private Date startTime;
    private Date endTime;
    private Integer timeType = 0;

    private Integer day;
}
