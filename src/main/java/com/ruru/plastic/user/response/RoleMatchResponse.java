package com.ruru.plastic.user.response;

import com.ruru.plastic.user.model.AdminUser;
import com.ruru.plastic.user.model.Role;
import com.ruru.plastic.user.model.RoleMatch;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class RoleMatchResponse extends RoleMatch implements Serializable {
    private AdminUser adminUser;
    private Role role;
}
