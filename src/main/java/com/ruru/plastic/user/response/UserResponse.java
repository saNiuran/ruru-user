package com.ruru.plastic.user.response;

import com.ruru.plastic.user.model.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class UserResponse extends User implements Serializable {
    private String token;
    private String adminToken;
    private String uid;         //第三方邓丽 用户编号
    private Integer thirdType;
    private Member member;
    private UserAccount userAccount;
    private Company company;
    private List<UserPropertyResponse> properties;
}
