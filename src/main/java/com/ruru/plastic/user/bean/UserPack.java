package com.ruru.plastic.user.bean;

import com.ruru.plastic.user.model.CertificateLog;
import com.ruru.plastic.user.response.UserResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class UserPack extends UserResponse implements Serializable {
    private UserCounter userCounter;
    private List<CertificateLog> certificateLogList;
}
