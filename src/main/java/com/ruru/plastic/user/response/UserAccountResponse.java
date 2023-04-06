package com.ruru.plastic.user.response;

import com.ruru.plastic.user.model.User;
import com.ruru.plastic.user.model.UserAccount;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class UserAccountResponse extends UserAccount implements Serializable {
    private User user;
}
