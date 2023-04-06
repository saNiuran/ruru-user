package com.ruru.plastic.user.response;

import com.ruru.plastic.user.model.PersonalCert;
import com.ruru.plastic.user.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class PersonalCertResponse extends PersonalCert implements Serializable {
    private User user;
}
