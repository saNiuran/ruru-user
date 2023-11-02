package com.ruru.plastic.user.response;

import com.ruru.plastic.user.model.UserMatch;
import com.ruru.plastic.user.model.UserWx;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class UserMatchResponse extends UserMatch implements Serializable {
    private UserResponse user;
    private UserWx userWx;
}
