package com.ruru.plastic.user.request;

import com.ruru.plastic.user.model.UserCorporateCertMatch;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class UserCorporateCertMatchRequest extends UserCorporateCertMatch implements Serializable {
    private Integer page;
    private Integer size;
}
