package com.ruru.plastic.user.request;

import com.ruru.plastic.user.model.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class RoleRequest extends Role implements Serializable {
    private Integer page = 1;
    private Integer size = 10;
    private String search;
}
