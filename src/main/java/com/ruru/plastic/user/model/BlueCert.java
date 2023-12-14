package com.ruru.plastic.user.model;

import java.util.Date;

public class BlueCert {
    private Long id;

    private Long userId;

    private String name;

    private String socialCode;

    private String licenceAddr;

    private Date setupTime;

    private String licenseImg;

    private String authorityImg;

    private Integer status;

    private String legalPerson;

    private String contactName;

    private String contactMobile;

    private String contactIdNo;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSocialCode() {
        return socialCode;
    }

    public void setSocialCode(String socialCode) {
        this.socialCode = socialCode == null ? null : socialCode.trim();
    }

    public String getLicenceAddr() {
        return licenceAddr;
    }

    public void setLicenceAddr(String licenceAddr) {
        this.licenceAddr = licenceAddr == null ? null : licenceAddr.trim();
    }

    public Date getSetupTime() {
        return setupTime;
    }

    public void setSetupTime(Date setupTime) {
        this.setupTime = setupTime;
    }

    public String getLicenseImg() {
        return licenseImg;
    }

    public void setLicenseImg(String licenseImg) {
        this.licenseImg = licenseImg == null ? null : licenseImg.trim();
    }

    public String getAuthorityImg() {
        return authorityImg;
    }

    public void setAuthorityImg(String authorityImg) {
        this.authorityImg = authorityImg == null ? null : authorityImg.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson == null ? null : legalPerson.trim();
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName == null ? null : contactName.trim();
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile == null ? null : contactMobile.trim();
    }

    public String getContactIdNo() {
        return contactIdNo;
    }

    public void setContactIdNo(String contactIdNo) {
        this.contactIdNo = contactIdNo == null ? null : contactIdNo.trim();
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