package com.ruru.plastic.user.model;

import java.util.Date;

public class CertificateLog {
    private Long id;

    private Long lordId;

    private Integer lordType;

    private Long userId;

    private Integer certLevel;

    private Integer certStatus;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLordId() {
        return lordId;
    }

    public void setLordId(Long lordId) {
        this.lordId = lordId;
    }

    public Integer getLordType() {
        return lordType;
    }

    public void setLordType(Integer lordType) {
        this.lordType = lordType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getCertLevel() {
        return certLevel;
    }

    public void setCertLevel(Integer certLevel) {
        this.certLevel = certLevel;
    }

    public Integer getCertStatus() {
        return certStatus;
    }

    public void setCertStatus(Integer certStatus) {
        this.certStatus = certStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}