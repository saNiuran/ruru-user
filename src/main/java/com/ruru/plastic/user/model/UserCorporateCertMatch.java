package com.ruru.plastic.user.model;

public class UserCorporateCertMatch {
    private Long id;

    private Long userId;

    private Long corporateCertId;

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
}