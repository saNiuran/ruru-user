package com.ruru.plastic.user.request;

import com.ruru.plastic.user.model.UserCorporateCertMatch;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class UserCorporateCertMatchRequest extends UserCorporateCertMatch implements Serializable {
    private Integer page;
    private Integer size;
    private Date startTime;
    private Date endTime;
    private String orderClause;
}
