package com.ruru.plastic.user.request;

import com.ruru.plastic.user.model.CorporateCert;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class CorporateCertRequest extends CorporateCert implements Serializable {
    private Integer page = 1;
    private Integer size = 10;
    private String orderClause;
    private Date startTime;
    private Date endTime;
}
