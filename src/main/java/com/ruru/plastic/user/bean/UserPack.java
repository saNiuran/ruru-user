package com.ruru.plastic.user.bean;

import com.ruru.plastic.user.model.Company;
import com.ruru.plastic.user.model.CorporateCert;
import com.ruru.plastic.user.model.PersonalCert;
import com.ruru.plastic.user.response.UserResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class UserPack extends UserResponse implements Serializable {
    private UserCounter userCounter;
    private PersonalCert personalCert;
    private CorporateCert corporateCert;
}
