package com.ruru.plastic.user.response;

import com.ruru.plastic.user.model.MemberLog;
import com.ruru.plastic.user.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class MemberLogResponse extends MemberLog implements Serializable {
    private User user;
}
