package com.ruru.plastic.user.request;

import com.ruru.plastic.user.model.UserChannel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class UserChannelRequest extends UserChannel implements Serializable {
    private Integer page = 1;
    private Integer size = 10;

    private Date startTime;
    private Date endTime;
}
