package com.ruru.plastic.user.request;

import com.ruru.plastic.user.model.Company;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class CompanyRequest extends Company implements Serializable {
    private Integer page;
    private Integer size;
    private String orderClause;
    private Date startTime;
    private Date endTime;
}
