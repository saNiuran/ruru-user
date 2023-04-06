package com.ruru.plastic.user.bean;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class MobileUpdateRequest implements Serializable {
    private String oldCode;
    private String newCode;
    private String newPhone;
}
