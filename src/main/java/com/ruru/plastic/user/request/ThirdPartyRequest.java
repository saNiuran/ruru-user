package com.ruru.plastic.user.request;

import com.ruru.plastic.user.model.ThirdParty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class ThirdPartyRequest extends ThirdParty implements Serializable {
    private String mobile;
    private String smsCode;
}
