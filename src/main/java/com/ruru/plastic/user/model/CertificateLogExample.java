package com.ruru.plastic.user.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CertificateLogExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CertificateLogExample() {
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

        public Criteria andLordIdIsNull() {
            addCriterion("lord_id is null");
            return (Criteria) this;
        }

        public Criteria andLordIdIsNotNull() {
            addCriterion("lord_id is not null");
            return (Criteria) this;
        }

        public Criteria andLordIdEqualTo(Long value) {
            addCriterion("lord_id =", value, "lordId");
            return (Criteria) this;
        }

        public Criteria andLordIdNotEqualTo(Long value) {
            addCriterion("lord_id <>", value, "lordId");
            return (Criteria) this;
        }

        public Criteria andLordIdGreaterThan(Long value) {
            addCriterion("lord_id >", value, "lordId");
            return (Criteria) this;
        }

        public Criteria andLordIdGreaterThanOrEqualTo(Long value) {
            addCriterion("lord_id >=", value, "lordId");
            return (Criteria) this;
        }

        public Criteria andLordIdLessThan(Long value) {
            addCriterion("lord_id <", value, "lordId");
            return (Criteria) this;
        }

        public Criteria andLordIdLessThanOrEqualTo(Long value) {
            addCriterion("lord_id <=", value, "lordId");
            return (Criteria) this;
        }

        public Criteria andLordIdIn(List<Long> values) {
            addCriterion("lord_id in", values, "lordId");
            return (Criteria) this;
        }

        public Criteria andLordIdNotIn(List<Long> values) {
            addCriterion("lord_id not in", values, "lordId");
            return (Criteria) this;
        }

        public Criteria andLordIdBetween(Long value1, Long value2) {
            addCriterion("lord_id between", value1, value2, "lordId");
            return (Criteria) this;
        }

        public Criteria andLordIdNotBetween(Long value1, Long value2) {
            addCriterion("lord_id not between", value1, value2, "lordId");
            return (Criteria) this;
        }

        public Criteria andLordTypeIsNull() {
            addCriterion("lord_type is null");
            return (Criteria) this;
        }

        public Criteria andLordTypeIsNotNull() {
            addCriterion("lord_type is not null");
            return (Criteria) this;
        }

        public Criteria andLordTypeEqualTo(Integer value) {
            addCriterion("lord_type =", value, "lordType");
            return (Criteria) this;
        }

        public Criteria andLordTypeNotEqualTo(Integer value) {
            addCriterion("lord_type <>", value, "lordType");
            return (Criteria) this;
        }

        public Criteria andLordTypeGreaterThan(Integer value) {
            addCriterion("lord_type >", value, "lordType");
            return (Criteria) this;
        }

        public Criteria andLordTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("lord_type >=", value, "lordType");
            return (Criteria) this;
        }

        public Criteria andLordTypeLessThan(Integer value) {
            addCriterion("lord_type <", value, "lordType");
            return (Criteria) this;
        }

        public Criteria andLordTypeLessThanOrEqualTo(Integer value) {
            addCriterion("lord_type <=", value, "lordType");
            return (Criteria) this;
        }

        public Criteria andLordTypeIn(List<Integer> values) {
            addCriterion("lord_type in", values, "lordType");
            return (Criteria) this;
        }

        public Criteria andLordTypeNotIn(List<Integer> values) {
            addCriterion("lord_type not in", values, "lordType");
            return (Criteria) this;
        }

        public Criteria andLordTypeBetween(Integer value1, Integer value2) {
            addCriterion("lord_type between", value1, value2, "lordType");
            return (Criteria) this;
        }

        public Criteria andLordTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("lord_type not between", value1, value2, "lordType");
            return (Criteria) this;
        }

        public Criteria andOperatorIdIsNull() {
            addCriterion("operator_id is null");
            return (Criteria) this;
        }

        public Criteria andOperatorIdIsNotNull() {
            addCriterion("operator_id is not null");
            return (Criteria) this;
        }

        public Criteria andOperatorIdEqualTo(Long value) {
            addCriterion("operator_id =", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdNotEqualTo(Long value) {
            addCriterion("operator_id <>", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdGreaterThan(Long value) {
            addCriterion("operator_id >", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdGreaterThanOrEqualTo(Long value) {
            addCriterion("operator_id >=", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdLessThan(Long value) {
            addCriterion("operator_id <", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdLessThanOrEqualTo(Long value) {
            addCriterion("operator_id <=", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdIn(List<Long> values) {
            addCriterion("operator_id in", values, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdNotIn(List<Long> values) {
            addCriterion("operator_id not in", values, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdBetween(Long value1, Long value2) {
            addCriterion("operator_id between", value1, value2, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdNotBetween(Long value1, Long value2) {
            addCriterion("operator_id not between", value1, value2, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorTypeIsNull() {
            addCriterion("operator_type is null");
            return (Criteria) this;
        }

        public Criteria andOperatorTypeIsNotNull() {
            addCriterion("operator_type is not null");
            return (Criteria) this;
        }

        public Criteria andOperatorTypeEqualTo(Integer value) {
            addCriterion("operator_type =", value, "operatorType");
            return (Criteria) this;
        }

        public Criteria andOperatorTypeNotEqualTo(Integer value) {
            addCriterion("operator_type <>", value, "operatorType");
            return (Criteria) this;
        }

        public Criteria andOperatorTypeGreaterThan(Integer value) {
            addCriterion("operator_type >", value, "operatorType");
            return (Criteria) this;
        }

        public Criteria andOperatorTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("operator_type >=", value, "operatorType");
            return (Criteria) this;
        }

        public Criteria andOperatorTypeLessThan(Integer value) {
            addCriterion("operator_type <", value, "operatorType");
            return (Criteria) this;
        }

        public Criteria andOperatorTypeLessThanOrEqualTo(Integer value) {
            addCriterion("operator_type <=", value, "operatorType");
            return (Criteria) this;
        }

        public Criteria andOperatorTypeIn(List<Integer> values) {
            addCriterion("operator_type in", values, "operatorType");
            return (Criteria) this;
        }

        public Criteria andOperatorTypeNotIn(List<Integer> values) {
            addCriterion("operator_type not in", values, "operatorType");
            return (Criteria) this;
        }

        public Criteria andOperatorTypeBetween(Integer value1, Integer value2) {
            addCriterion("operator_type between", value1, value2, "operatorType");
            return (Criteria) this;
        }

        public Criteria andOperatorTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("operator_type not between", value1, value2, "operatorType");
            return (Criteria) this;
        }

        public Criteria andCertLevelIsNull() {
            addCriterion("cert_level is null");
            return (Criteria) this;
        }

        public Criteria andCertLevelIsNotNull() {
            addCriterion("cert_level is not null");
            return (Criteria) this;
        }

        public Criteria andCertLevelEqualTo(Integer value) {
            addCriterion("cert_level =", value, "certLevel");
            return (Criteria) this;
        }

        public Criteria andCertLevelNotEqualTo(Integer value) {
            addCriterion("cert_level <>", value, "certLevel");
            return (Criteria) this;
        }

        public Criteria andCertLevelGreaterThan(Integer value) {
            addCriterion("cert_level >", value, "certLevel");
            return (Criteria) this;
        }

        public Criteria andCertLevelGreaterThanOrEqualTo(Integer value) {
            addCriterion("cert_level >=", value, "certLevel");
            return (Criteria) this;
        }

        public Criteria andCertLevelLessThan(Integer value) {
            addCriterion("cert_level <", value, "certLevel");
            return (Criteria) this;
        }

        public Criteria andCertLevelLessThanOrEqualTo(Integer value) {
            addCriterion("cert_level <=", value, "certLevel");
            return (Criteria) this;
        }

        public Criteria andCertLevelIn(List<Integer> values) {
            addCriterion("cert_level in", values, "certLevel");
            return (Criteria) this;
        }

        public Criteria andCertLevelNotIn(List<Integer> values) {
            addCriterion("cert_level not in", values, "certLevel");
            return (Criteria) this;
        }

        public Criteria andCertLevelBetween(Integer value1, Integer value2) {
            addCriterion("cert_level between", value1, value2, "certLevel");
            return (Criteria) this;
        }

        public Criteria andCertLevelNotBetween(Integer value1, Integer value2) {
            addCriterion("cert_level not between", value1, value2, "certLevel");
            return (Criteria) this;
        }

        public Criteria andCertStatusIsNull() {
            addCriterion("cert_status is null");
            return (Criteria) this;
        }

        public Criteria andCertStatusIsNotNull() {
            addCriterion("cert_status is not null");
            return (Criteria) this;
        }

        public Criteria andCertStatusEqualTo(Integer value) {
            addCriterion("cert_status =", value, "certStatus");
            return (Criteria) this;
        }

        public Criteria andCertStatusNotEqualTo(Integer value) {
            addCriterion("cert_status <>", value, "certStatus");
            return (Criteria) this;
        }

        public Criteria andCertStatusGreaterThan(Integer value) {
            addCriterion("cert_status >", value, "certStatus");
            return (Criteria) this;
        }

        public Criteria andCertStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("cert_status >=", value, "certStatus");
            return (Criteria) this;
        }

        public Criteria andCertStatusLessThan(Integer value) {
            addCriterion("cert_status <", value, "certStatus");
            return (Criteria) this;
        }

        public Criteria andCertStatusLessThanOrEqualTo(Integer value) {
            addCriterion("cert_status <=", value, "certStatus");
            return (Criteria) this;
        }

        public Criteria andCertStatusIn(List<Integer> values) {
            addCriterion("cert_status in", values, "certStatus");
            return (Criteria) this;
        }

        public Criteria andCertStatusNotIn(List<Integer> values) {
            addCriterion("cert_status not in", values, "certStatus");
            return (Criteria) this;
        }

        public Criteria andCertStatusBetween(Integer value1, Integer value2) {
            addCriterion("cert_status between", value1, value2, "certStatus");
            return (Criteria) this;
        }

        public Criteria andCertStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("cert_status not between", value1, value2, "certStatus");
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