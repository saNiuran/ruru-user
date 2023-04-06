package com.ruru.plastic.user.request;

import com.ruru.plastic.user.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class LogonRequest extends User implements Serializable {
    private String smsCode;
}
