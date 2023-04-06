package com.ruru.plastic.user.request;

import com.ruru.plastic.user.model.AdminUser;
import com.ruru.plastic.user.model.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class AdminUserRequest extends AdminUser implements Serializable {
    private String oldPassword;
    private Integer page = 1;
    private Integer size = 10;
    private Date startTime;
    private Date endTime;
    private String orderClause = "update_time desc";
    private String search;
    private String smsCode;
    private Integer force;  //1=强制，不校验旧的密码
    private Integer roleForce; //1=强制，更新角色列表

    private List<Role> roles;

}
