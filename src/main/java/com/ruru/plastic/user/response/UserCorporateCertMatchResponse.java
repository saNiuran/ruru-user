package com.ruru.plastic.user.response;

import com.ruru.plastic.user.model.CorporateCert;
import com.ruru.plastic.user.model.User;
import com.ruru.plastic.user.model.UserCorporateCertMatch;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class UserCorporateCertMatchResponse extends UserCorporateCertMatch implements Serializable {
    private CorporateCert corporateCert;
    private User user;
}
