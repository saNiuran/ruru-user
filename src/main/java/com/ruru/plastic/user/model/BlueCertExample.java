package com.ruru.plastic.user.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class BlueCertExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public BlueCertExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Long value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Long value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Long value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Long value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Long value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Long> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Long> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Long value1, Long value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Long value1, Long value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("`name` is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("`name` is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("`name` =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("`name` <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("`name` >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("`name` >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("`name` <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("`name` <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("`name` like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("`name` not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("`name` in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("`name` not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("`name` between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("`name` not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andSocialCodeIsNull() {
            addCriterion("social_code is null");
            return (Criteria) this;
        }

        public Criteria andSocialCodeIsNotNull() {
            addCriterion("social_code is not null");
            return (Criteria) this;
        }

        public Criteria andSocialCodeEqualTo(String value) {
            addCriterion("social_code =", value, "socialCode");
            return (Criteria) this;
        }

        public Criteria andSocialCodeNotEqualTo(String value) {
            addCriterion("social_code <>", value, "socialCode");
            return (Criteria) this;
        }

        public Criteria andSocialCodeGreaterThan(String value) {
            addCriterion("social_code >", value, "socialCode");
            return (Criteria) this;
        }

        public Criteria andSocialCodeGreaterThanOrEqualTo(String value) {
            addCriterion("social_code >=", value, "socialCode");
            return (Criteria) this;
        }

        public Criteria andSocialCodeLessThan(String value) {
            addCriterion("social_code <", value, "socialCode");
            return (Criteria) this;
        }

        public Criteria andSocialCodeLessThanOrEqualTo(String value) {
            addCriterion("social_code <=", value, "socialCode");
            return (Criteria) this;
        }

        public Criteria andSocialCodeLike(String value) {
            addCriterion("social_code like", value, "socialCode");
            return (Criteria) this;
        }

        public Criteria andSocialCodeNotLike(String value) {
            addCriterion("social_code not like", value, "socialCode");
            return (Criteria) this;
        }

        public Criteria andSocialCodeIn(List<String> values) {
            addCriterion("social_code in", values, "socialCode");
            return (Criteria) this;
        }

        public Criteria andSocialCodeNotIn(List<String> values) {
            addCriterion("social_code not in", values, "socialCode");
            return (Criteria) this;
        }

        public Criteria andSocialCodeBetween(String value1, String value2) {
            addCriterion("social_code between", value1, value2, "socialCode");
            return (Criteria) this;
        }

        public Criteria andSocialCodeNotBetween(String value1, String value2) {
            addCriterion("social_code not between", value1, value2, "socialCode");
            return (Criteria) this;
        }

        public Criteria andLicenceAddrIsNull() {
            addCriterion("licence_addr is null");
            return (Criteria) this;
        }

        public Criteria andLicenceAddrIsNotNull() {
            addCriterion("licence_addr is not null");
            return (Criteria) this;
        }

        public Criteria andLicenceAddrEqualTo(String value) {
            addCriterion("licence_addr =", value, "licenceAddr");
            return (Criteria) this;
        }

        public Criteria andLicenceAddrNotEqualTo(String value) {
            addCriterion("licence_addr <>", value, "licenceAddr");
            return (Criteria) this;
        }

        public Criteria andLicenceAddrGreaterThan(String value) {
            addCriterion("licence_addr >", value, "licenceAddr");
            return (Criteria) this;
        }

        public Criteria andLicenceAddrGreaterThanOrEqualTo(String value) {
            addCriterion("licence_addr >=", value, "licenceAddr");
            return (Criteria) this;
        }

        public Criteria andLicenceAddrLessThan(String value) {
            addCriterion("licence_addr <", value, "licenceAddr");
            return (Criteria) this;
        }

        public Criteria andLicenceAddrLessThanOrEqualTo(String value) {
            addCriterion("licence_addr <=", value, "licenceAddr");
            return (Criteria) this;
        }

        public Criteria andLicenceAddrLike(String value) {
            addCriterion("licence_addr like", value, "licenceAddr");
            return (Criteria) this;
        }

        public Criteria andLicenceAddrNotLike(String value) {
            addCriterion("licence_addr not like", value, "licenceAddr");
            return (Criteria) this;
        }

        public Criteria andLicenceAddrIn(List<String> values) {
            addCriterion("licence_addr in", values, "licenceAddr");
            return (Criteria) this;
        }

        public Criteria andLicenceAddrNotIn(List<String> values) {
            addCriterion("licence_addr not in", values, "licenceAddr");
            return (Criteria) this;
        }

        public Criteria andLicenceAddrBetween(String value1, String value2) {
            addCriterion("licence_addr between", value1, value2, "licenceAddr");
            return (Criteria) this;
        }

        public Criteria andLicenceAddrNotBetween(String value1, String value2) {
            addCriterion("licence_addr not between", value1, value2, "licenceAddr");
            return (Criteria) this;
        }

        public Criteria andSetupTimeIsNull() {
            addCriterion("setup_time is null");
            return (Criteria) this;
        }

        public Criteria andSetupTimeIsNotNull() {
            addCriterion("setup_time is not null");
            return (Criteria) this;
        }

        public Criteria andSetupTimeEqualTo(Date value) {
            addCriterionForJDBCDate("setup_time =", value, "setupTime");
            return (Criteria) this;
        }

        public Criteria andSetupTimeNotEqualTo(Date value) {
            addCriterionForJDBCDate("setup_time <>", value, "setupTime");
            return (Criteria) this;
        }

        public Criteria andSetupTimeGreaterThan(Date value) {
            addCriterionForJDBCDate("setup_time >", value, "setupTime");
            return (Criteria) this;
        }

        public Criteria andSetupTimeGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("setup_time >=", value, "setupTime");
            return (Criteria) this;
        }

        public Criteria andSetupTimeLessThan(Date value) {
            addCriterionForJDBCDate("setup_time <", value, "setupTime");
            return (Criteria) this;
        }

        public Criteria andSetupTimeLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("setup_time <=", value, "setupTime");
            return (Criteria) this;
        }

        public Criteria andSetupTimeIn(List<Date> values) {
            addCriterionForJDBCDate("setup_time in", values, "setupTime");
            return (Criteria) this;
        }

        public Criteria andSetupTimeNotIn(List<Date> values) {
            addCriterionForJDBCDate("setup_time not in", values, "setupTime");
            return (Criteria) this;
        }

        public Criteria andSetupTimeBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("setup_time between", value1, value2, "setupTime");
            return (Criteria) this;
        }

        public Criteria andSetupTimeNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("setup_time not between", value1, value2, "setupTime");
            return (Criteria) this;
        }

        public Criteria andLicenseImgIsNull() {
            addCriterion("license_img is null");
            return (Criteria) this;
        }

        public Criteria andLicenseImgIsNotNull() {
            addCriterion("license_img is not null");
            return (Criteria) this;
        }

        public Criteria andLicenseImgEqualTo(String value) {
            addCriterion("license_img =", value, "licenseImg");
            return (Criteria) this;
        }

        public Criteria andLicenseImgNotEqualTo(String value) {
            addCriterion("license_img <>", value, "licenseImg");
            return (Criteria) this;
        }

        public Criteria andLicenseImgGreaterThan(String value) {
            addCriterion("license_img >", value, "licenseImg");
            return (Criteria) this;
        }

        public Criteria andLicenseImgGreaterThanOrEqualTo(String value) {
            addCriterion("license_img >=", value, "licenseImg");
            return (Criteria) this;
        }

        public Criteria andLicenseImgLessThan(String value) {
            addCriterion("license_img <", value, "licenseImg");
            return (Criteria) this;
        }

        public Criteria andLicenseImgLessThanOrEqualTo(String value) {
            addCriterion("license_img <=", value, "licenseImg");
            return (Criteria) this;
        }

        public Criteria andLicenseImgLike(String value) {
            addCriterion("license_img like", value, "licenseImg");
            return (Criteria) this;
        }

        public Criteria andLicenseImgNotLike(String value) {
            addCriterion("license_img not like", value, "licenseImg");
            return (Criteria) this;
        }

        public Criteria andLicenseImgIn(List<String> values) {
            addCriterion("license_img in", values, "licenseImg");
            return (Criteria) this;
        }

        public Criteria andLicenseImgNotIn(List<String> values) {
            addCriterion("license_img not in", values, "licenseImg");
            return (Criteria) this;
        }

        public Criteria andLicenseImgBetween(String value1, String value2) {
            addCriterion("license_img between", value1, value2, "licenseImg");
            return (Criteria) this;
        }

        public Criteria andLicenseImgNotBetween(String value1, String value2) {
            addCriterion("license_img not between", value1, value2, "licenseImg");
            return (Criteria) this;
        }

        public Criteria andAuthorityImgIsNull() {
            addCriterion("authority_img is null");
            return (Criteria) this;
        }

        public Criteria andAuthorityImgIsNotNull() {
            addCriterion("authority_img is not null");
            return (Criteria) this;
        }

        public Criteria andAuthorityImgEqualTo(String value) {
            addCriterion("authority_img =", value, "authorityImg");
            return (Criteria) this;
        }

        public Criteria andAuthorityImgNotEqualTo(String value) {
            addCriterion("authority_img <>", value, "authorityImg");
            return (Criteria) this;
        }

        public Criteria andAuthorityImgGreaterThan(String value) {
            addCriterion("authority_img >", value, "authorityImg");
            return (Criteria) this;
        }

        public Criteria andAuthorityImgGreaterThanOrEqualTo(String value) {
            addCriterion("authority_img >=", value, "authorityImg");
            return (Criteria) this;
        }

        public Criteria andAuthorityImgLessThan(String value) {
            addCriterion("authority_img <", value, "authorityImg");
            return (Criteria) this;
        }

        public Criteria andAuthorityImgLessThanOrEqualTo(String value) {
            addCriterion("authority_img <=", value, "authorityImg");
            return (Criteria) this;
        }

        public Criteria andAuthorityImgLike(String value) {
            addCriterion("authority_img like", value, "authorityImg");
            return (Criteria) this;
        }

        public Criteria andAuthorityImgNotLike(String value) {
            addCriterion("authority_img not like", value, "authorityImg");
            return (Criteria) this;
        }

        public Criteria andAuthorityImgIn(List<String> values) {
            addCriterion("authority_img in", values, "authorityImg");
            return (Criteria) this;
        }

        public Criteria andAuthorityImgNotIn(List<String> values) {
            addCriterion("authority_img not in", values, "authorityImg");
            return (Criteria) this;
        }

        public Criteria andAuthorityImgBetween(String value1, String value2) {
            addCriterion("authority_img between", value1, value2, "authorityImg");
            return (Criteria) this;
        }

        public Criteria andAuthorityImgNotBetween(String value1, String value2) {
            addCriterion("authority_img not between", value1, value2, "authorityImg");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("`status` is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("`status` is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("`status` =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("`status` <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("`status` >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("`status` >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("`status` <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("`status` <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("`status` in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("`status` not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("`status` between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("`status` not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andLegalPersonIsNull() {
            addCriterion("legal_person is null");
            return (Criteria) this;
        }

        public Criteria andLegalPersonIsNotNull() {
            addCriterion("legal_person is not null");
            return (Criteria) this;
        }

        public Criteria andLegalPersonEqualTo(String value) {
            addCriterion("legal_person =", value, "legalPerson");
            return (Criteria) this;
        }

        public Criteria andLegalPersonNotEqualTo(String value) {
            addCriterion("legal_person <>", value, "legalPerson");
            return (Criteria) this;
        }

        public Criteria andLegalPersonGreaterThan(String value) {
            addCriterion("legal_person >", value, "legalPerson");
            return (Criteria) this;
        }

        public Criteria andLegalPersonGreaterThanOrEqualTo(String value) {
            addCriterion("legal_person >=", value, "legalPerson");
            return (Criteria) this;
        }

        public Criteria andLegalPersonLessThan(String value) {
            addCriterion("legal_person <", value, "legalPerson");
            return (Criteria) this;
        }

        public Criteria andLegalPersonLessThanOrEqualTo(String value) {
            addCriterion("legal_person <=", value, "legalPerson");
            return (Criteria) this;
        }

        public Criteria andLegalPersonLike(String value) {
            addCriterion("legal_person like", value, "legalPerson");
            return (Criteria) this;
        }

        public Criteria andLegalPersonNotLike(String value) {
            addCriterion("legal_person not like", value, "legalPerson");
            return (Criteria) this;
        }

        public Criteria andLegalPersonIn(List<String> values) {
            addCriterion("legal_person in", values, "legalPerson");
            return (Criteria) this;
        }

        public Criteria andLegalPersonNotIn(List<String> values) {
            addCriterion("legal_person not in", values, "legalPerson");
            return (Criteria) this;
        }

        public Criteria andLegalPersonBetween(String value1, String value2) {
            addCriterion("legal_person between", value1, value2, "legalPerson");
            return (Criteria) this;
        }

        public Criteria andLegalPersonNotBetween(String value1, String value2) {
            addCriterion("legal_person not between", value1, value2, "legalPerson");
            return (Criteria) this;
        }

        public Criteria andContactNameIsNull() {
            addCriterion("contact_name is null");
            return (Criteria) this;
        }

        public Criteria andContactNameIsNotNull() {
            addCriterion("contact_name is not null");
            return (Criteria) this;
        }

        public Criteria andContactNameEqualTo(String value) {
            addCriterion("contact_name =", value, "contactName");
            return (Criteria) this;
        }

        public Criteria andContactNameNotEqualTo(String value) {
            addCriterion("contact_name <>", value, "contactName");
            return (Criteria) this;
        }

        public Criteria andContactNameGreaterThan(String value) {
            addCriterion("contact_name >", value, "contactName");
            return (Criteria) this;
        }

        public Criteria andContactNameGreaterThanOrEqualTo(String value) {
            addCriterion("contact_name >=", value, "contactName");
            return (Criteria) this;
        }

        public Criteria andContactNameLessThan(String value) {
            addCriterion("contact_name <", value, "contactName");
            return (Criteria) this;
        }

        public Criteria andContactNameLessThanOrEqualTo(String value) {
            addCriterion("contact_name <=", value, "contactName");
            return (Criteria) this;
        }

        public Criteria andContactNameLike(String value) {
            addCriterion("contact_name like", value, "contactName");
            return (Criteria) this;
        }

        public Criteria andContactNameNotLike(String value) {
            addCriterion("contact_name not like", value, "contactName");
            return (Criteria) this;
        }

        public Criteria andContactNameIn(List<String> values) {
            addCriterion("contact_name in", values, "contactName");
            return (Criteria) this;
        }

        public Criteria andContactNameNotIn(List<String> values) {
            addCriterion("contact_name not in", values, "contactName");
            return (Criteria) this;
        }

        public Criteria andContactNameBetween(String value1, String value2) {
            addCriterion("contact_name between", value1, value2, "contactName");
            return (Criteria) this;
        }

        public Criteria andContactNameNotBetween(String value1, String value2) {
            addCriterion("contact_name not between", value1, value2, "contactName");
            return (Criteria) this;
        }

        public Criteria andContactMobileIsNull() {
            addCriterion("contact_mobile is null");
            return (Criteria) this;
        }

        public Criteria andContactMobileIsNotNull() {
            addCriterion("contact_mobile is not null");
            return (Criteria) this;
        }

        public Criteria andContactMobileEqualTo(String value) {
            addCriterion("contact_mobile =", value, "contactMobile");
            return (Criteria) this;
        }

        public Criteria andContactMobileNotEqualTo(String value) {
            addCriterion("contact_mobile <>", value, "contactMobile");
            return (Criteria) this;
        }

        public Criteria andContactMobileGreaterThan(String value) {
            addCriterion("contact_mobile >", value, "contactMobile");
            return (Criteria) this;
        }

        public Criteria andContactMobileGreaterThanOrEqualTo(String value) {
            addCriterion("contact_mobile >=", value, "contactMobile");
            return (Criteria) this;
        }

        public Criteria andContactMobileLessThan(String value) {
            addCriterion("contact_mobile <", value, "contactMobile");
            return (Criteria) this;
        }

        public Criteria andContactMobileLessThanOrEqualTo(String value) {
            addCriterion("contact_mobile <=", value, "contactMobile");
            return (Criteria) this;
        }

        public Criteria andContactMobileLike(String value) {
            addCriterion("contact_mobile like", value, "contactMobile");
            return (Criteria) this;
        }

        public Criteria andContactMobileNotLike(String value) {
            addCriterion("contact_mobile not like", value, "contactMobile");
            return (Criteria) this;
        }

        public Criteria andContactMobileIn(List<String> values) {
            addCriterion("contact_mobile in", values, "contactMobile");
            return (Criteria) this;
        }

        public Criteria andContactMobileNotIn(List<String> values) {
            addCriterion("contact_mobile not in", values, "contactMobile");
            return (Criteria) this;
        }

        public Criteria andContactMobileBetween(String value1, String value2) {
            addCriterion("contact_mobile between", value1, value2, "contactMobile");
            return (Criteria) this;
        }

        public Criteria andContactMobileNotBetween(String value1, String value2) {
            addCriterion("contact_mobile not between", value1, value2, "contactMobile");
            return (Criteria) this;
        }

        public Criteria andContactIdNoIsNull() {
            addCriterion("contact_id_no is null");
            return (Criteria) this;
        }

        public Criteria andContactIdNoIsNotNull() {
            addCriterion("contact_id_no is not null");
            return (Criteria) this;
        }

        public Criteria andContactIdNoEqualTo(String value) {
            addCriterion("contact_id_no =", value, "contactIdNo");
            return (Criteria) this;
        }

        public Criteria andContactIdNoNotEqualTo(String value) {
            addCriterion("contact_id_no <>", value, "contactIdNo");
            return (Criteria) this;
        }

        public Criteria andContactIdNoGreaterThan(String value) {
            addCriterion("contact_id_no >", value, "contactIdNo");
            return (Criteria) this;
        }

        public Criteria andContactIdNoGreaterThanOrEqualTo(String value) {
            addCriterion("contact_id_no >=", value, "contactIdNo");
            return (Criteria) this;
        }

        public Criteria andContactIdNoLessThan(String value) {
            addCriterion("contact_id_no <", value, "contactIdNo");
            return (Criteria) this;
        }

        public Criteria andContactIdNoLessThanOrEqualTo(String value) {
            addCriterion("contact_id_no <=", value, "contactIdNo");
            return (Criteria) this;
        }

        public Criteria andContactIdNoLike(String value) {
            addCriterion("contact_id_no like", value, "contactIdNo");
            return (Criteria) this;
        }

        public Criteria andContactIdNoNotLike(String value) {
            addCriterion("contact_id_no not like", value, "contactIdNo");
            return (Criteria) this;
        }

        public Criteria andContactIdNoIn(List<String> values) {
            addCriterion("contact_id_no in", values, "contactIdNo");
            return (Criteria) this;
        }

        public Criteria andContactIdNoNotIn(List<String> values) {
            addCriterion("contact_id_no not in", values, "contactIdNo");
            return (Criteria) this;
        }

        public Criteria andContactIdNoBetween(String value1, String value2) {
            addCriterion("contact_id_no between", value1, value2, "contactIdNo");
            return (Criteria) this;
        }

        public Criteria andContactIdNoNotBetween(String value1, String value2) {
            addCriterion("contact_id_no not between", value1, value2, "contactIdNo");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}