package com.ruru.plastic.user.bean;

public class Privilege {
    private Long id;

    private Integer action;

    private Integer memberLevel;

    private Long depositConfigId;

    private Integer value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public Integer getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(Integer memberLevel) {
        this.memberLevel = memberLevel;
    }

    public Long getDepositConfigId() {
        return depositConfigId;
    }

    public void setDepositConfigId(Long depositConfigId) {
        this.depositConfigId = depositConfigId;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}