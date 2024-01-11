package com.ruru.plastic.user.response;

import com.ruru.plastic.user.model.Property;
import com.ruru.plastic.user.model.UserProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class UserPropertyResponse extends UserProperty implements Serializable {
    private Property property;
}
