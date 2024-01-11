package com.ruru.plastic.user.request;

import com.ruru.plastic.user.model.BlueCert;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class BlueCertRequest extends BlueCert implements Serializable {
    private Integer page = 1;
    private Integer size = 10;
    private String orderClause;
    private Date startTime;
    private Date endTime;

    private List<Integer> statusList;
}
