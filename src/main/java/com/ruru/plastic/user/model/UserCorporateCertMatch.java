package com.ruru.plastic.user.model;

import java.util.Date;

public class UserCorporateCertMatch {
    private Long id;

    private Long userId;

    private Long corporateCertId;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCorporateCertId() {
        return corporateCertId;
    }

    public void setCorporateCertId(Long corporateCertId) {
        this.corporateCertId = corporateCertId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}