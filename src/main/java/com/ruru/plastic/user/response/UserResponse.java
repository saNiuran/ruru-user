package com.ruru.plastic.user.response;

import com.ruru.plastic.user.model.Company;
import com.ruru.plastic.user.model.Member;
import com.ruru.plastic.user.model.User;
import com.ruru.plastic.user.model.UserAccount;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class UserResponse extends User implements Serializable {
    private String token;
    private String uid;         //第三方邓丽 用户编号
    private Member member;
    private UserAccount userAccount;
    private Company company;
}
