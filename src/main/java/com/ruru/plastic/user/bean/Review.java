package com.ruru.plastic.user.bean;

import java.util.Date;

public class Review {
    private Long id;

    private Long lordId;

    private Integer lordType;

    private Long entityOwner;

    private String content;

    private Long reviewerId;

    private Long parentId;

    private Date createTime;

    private Integer status;

    private String ip;

    private String city;

    private Integer read;

    private Date readTime;

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

    public Long getEntityOwner() {
        return entityOwner;
    }

    public void setEntityOwner(Long entityOwner) {
        this.entityOwner = entityOwner;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public Integer getRead() {
        return read;
    }

    public void setRead(Integer read) {
        this.read = read;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }
}