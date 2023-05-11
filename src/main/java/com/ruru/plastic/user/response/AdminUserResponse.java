package com.ruru.plastic.user.response;

import com.ruru.plastic.user.model.AdminUser;
import com.ruru.plastic.user.model.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class AdminUserResponse extends AdminUser implements Serializable {
    private String adminToken;
    private List<Role> roles;
}
