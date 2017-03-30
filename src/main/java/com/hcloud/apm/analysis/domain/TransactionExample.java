package com.hcloud.apm.analysis.domain;

import java.util.ArrayList;
import java.util.List;

public class TransactionExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TransactionExample() {
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

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andModelNameIsNull() {
            addCriterion("modelName is null");
            return (Criteria) this;
        }

        public Criteria andModelNameIsNotNull() {
            addCriterion("modelName is not null");
            return (Criteria) this;
        }

        public Criteria andModelNameEqualTo(String value) {
            addCriterion("modelName =", value, "modelName");
            return (Criteria) this;
        }

        public Criteria andModelNameNotEqualTo(String value) {
            addCriterion("modelName <>", value, "modelName");
            return (Criteria) this;
        }

        public Criteria andModelNameGreaterThan(String value) {
            addCriterion("modelName >", value, "modelName");
            return (Criteria) this;
        }

        public Criteria andModelNameGreaterThanOrEqualTo(String value) {
            addCriterion("modelName >=", value, "modelName");
            return (Criteria) this;
        }

        public Criteria andModelNameLessThan(String value) {
            addCriterion("modelName <", value, "modelName");
            return (Criteria) this;
        }

        public Criteria andModelNameLessThanOrEqualTo(String value) {
            addCriterion("modelName <=", value, "modelName");
            return (Criteria) this;
        }

        public Criteria andModelNameLike(String value) {
            addCriterion("modelName like", value, "modelName");
            return (Criteria) this;
        }

        public Criteria andModelNameNotLike(String value) {
            addCriterion("modelName not like", value, "modelName");
            return (Criteria) this;
        }

        public Criteria andModelNameIn(List<String> values) {
            addCriterion("modelName in", values, "modelName");
            return (Criteria) this;
        }

        public Criteria andModelNameNotIn(List<String> values) {
            addCriterion("modelName not in", values, "modelName");
            return (Criteria) this;
        }

        public Criteria andModelNameBetween(String value1, String value2) {
            addCriterion("modelName between", value1, value2, "modelName");
            return (Criteria) this;
        }

        public Criteria andModelNameNotBetween(String value1, String value2) {
            addCriterion("modelName not between", value1, value2, "modelName");
            return (Criteria) this;
        }

        public Criteria andModelParamsIsNull() {
            addCriterion("modelParams is null");
            return (Criteria) this;
        }

        public Criteria andModelParamsIsNotNull() {
            addCriterion("modelParams is not null");
            return (Criteria) this;
        }

        public Criteria andModelParamsEqualTo(String value) {
            addCriterion("modelParams =", value, "modelParams");
            return (Criteria) this;
        }

        public Criteria andModelParamsNotEqualTo(String value) {
            addCriterion("modelParams <>", value, "modelParams");
            return (Criteria) this;
        }

        public Criteria andModelParamsGreaterThan(String value) {
            addCriterion("modelParams >", value, "modelParams");
            return (Criteria) this;
        }

        public Criteria andModelParamsGreaterThanOrEqualTo(String value) {
            addCriterion("modelParams >=", value, "modelParams");
            return (Criteria) this;
        }

        public Criteria andModelParamsLessThan(String value) {
            addCriterion("modelParams <", value, "modelParams");
            return (Criteria) this;
        }

        public Criteria andModelParamsLessThanOrEqualTo(String value) {
            addCriterion("modelParams <=", value, "modelParams");
            return (Criteria) this;
        }

        public Criteria andModelParamsLike(String value) {
            addCriterion("modelParams like", value, "modelParams");
            return (Criteria) this;
        }

        public Criteria andModelParamsNotLike(String value) {
            addCriterion("modelParams not like", value, "modelParams");
            return (Criteria) this;
        }

        public Criteria andModelParamsIn(List<String> values) {
            addCriterion("modelParams in", values, "modelParams");
            return (Criteria) this;
        }

        public Criteria andModelParamsNotIn(List<String> values) {
            addCriterion("modelParams not in", values, "modelParams");
            return (Criteria) this;
        }

        public Criteria andModelParamsBetween(String value1, String value2) {
            addCriterion("modelParams between", value1, value2, "modelParams");
            return (Criteria) this;
        }

        public Criteria andModelParamsNotBetween(String value1, String value2) {
            addCriterion("modelParams not between", value1, value2, "modelParams");
            return (Criteria) this;
        }

        public Criteria andStartTimeIsNull() {
            addCriterion("startTime is null");
            return (Criteria) this;
        }

        public Criteria andStartTimeIsNotNull() {
            addCriterion("startTime is not null");
            return (Criteria) this;
        }

        public Criteria andStartTimeEqualTo(String value) {
            addCriterion("startTime =", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotEqualTo(String value) {
            addCriterion("startTime <>", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeGreaterThan(String value) {
            addCriterion("startTime >", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeGreaterThanOrEqualTo(String value) {
            addCriterion("startTime >=", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLessThan(String value) {
            addCriterion("startTime <", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLessThanOrEqualTo(String value) {
            addCriterion("startTime <=", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLike(String value) {
            addCriterion("startTime like", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotLike(String value) {
            addCriterion("startTime not like", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeIn(List<String> values) {
            addCriterion("startTime in", values, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotIn(List<String> values) {
            addCriterion("startTime not in", values, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeBetween(String value1, String value2) {
            addCriterion("startTime between", value1, value2, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotBetween(String value1, String value2) {
            addCriterion("startTime not between", value1, value2, "startTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNull() {
            addCriterion("endTime is null");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNotNull() {
            addCriterion("endTime is not null");
            return (Criteria) this;
        }

        public Criteria andEndTimeEqualTo(String value) {
            addCriterion("endTime =", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotEqualTo(String value) {
            addCriterion("endTime <>", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThan(String value) {
            addCriterion("endTime >", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThanOrEqualTo(String value) {
            addCriterion("endTime >=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThan(String value) {
            addCriterion("endTime <", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThanOrEqualTo(String value) {
            addCriterion("endTime <=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLike(String value) {
            addCriterion("endTime like", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotLike(String value) {
            addCriterion("endTime not like", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIn(List<String> values) {
            addCriterion("endTime in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotIn(List<String> values) {
            addCriterion("endTime not in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeBetween(String value1, String value2) {
            addCriterion("endTime between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotBetween(String value1, String value2) {
            addCriterion("endTime not between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andDataSourceidIsNull() {
            addCriterion("dataSourceid is null");
            return (Criteria) this;
        }

        public Criteria andDataSourceidIsNotNull() {
            addCriterion("dataSourceid is not null");
            return (Criteria) this;
        }

        public Criteria andDataSourceidEqualTo(Integer value) {
            addCriterion("dataSourceid =", value, "dataSourceid");
            return (Criteria) this;
        }

        public Criteria andDataSourceidNotEqualTo(Integer value) {
            addCriterion("dataSourceid <>", value, "dataSourceid");
            return (Criteria) this;
        }

        public Criteria andDataSourceidGreaterThan(Integer value) {
            addCriterion("dataSourceid >", value, "dataSourceid");
            return (Criteria) this;
        }

        public Criteria andDataSourceidGreaterThanOrEqualTo(Integer value) {
            addCriterion("dataSourceid >=", value, "dataSourceid");
            return (Criteria) this;
        }

        public Criteria andDataSourceidLessThan(Integer value) {
            addCriterion("dataSourceid <", value, "dataSourceid");
            return (Criteria) this;
        }

        public Criteria andDataSourceidLessThanOrEqualTo(Integer value) {
            addCriterion("dataSourceid <=", value, "dataSourceid");
            return (Criteria) this;
        }

        public Criteria andDataSourceidIn(List<Integer> values) {
            addCriterion("dataSourceid in", values, "dataSourceid");
            return (Criteria) this;
        }

        public Criteria andDataSourceidNotIn(List<Integer> values) {
            addCriterion("dataSourceid not in", values, "dataSourceid");
            return (Criteria) this;
        }

        public Criteria andDataSourceidBetween(Integer value1, Integer value2) {
            addCriterion("dataSourceid between", value1, value2, "dataSourceid");
            return (Criteria) this;
        }

        public Criteria andDataSourceidNotBetween(Integer value1, Integer value2) {
            addCriterion("dataSourceid not between", value1, value2, "dataSourceid");
            return (Criteria) this;
        }

        public Criteria andLabelSnapshotIsNull() {
            addCriterion("labelSnapshot is null");
            return (Criteria) this;
        }

        public Criteria andLabelSnapshotIsNotNull() {
            addCriterion("labelSnapshot is not null");
            return (Criteria) this;
        }

        public Criteria andLabelSnapshotEqualTo(String value) {
            addCriterion("labelSnapshot =", value, "labelSnapshot");
            return (Criteria) this;
        }

        public Criteria andLabelSnapshotNotEqualTo(String value) {
            addCriterion("labelSnapshot <>", value, "labelSnapshot");
            return (Criteria) this;
        }

        public Criteria andLabelSnapshotGreaterThan(String value) {
            addCriterion("labelSnapshot >", value, "labelSnapshot");
            return (Criteria) this;
        }

        public Criteria andLabelSnapshotGreaterThanOrEqualTo(String value) {
            addCriterion("labelSnapshot >=", value, "labelSnapshot");
            return (Criteria) this;
        }

        public Criteria andLabelSnapshotLessThan(String value) {
            addCriterion("labelSnapshot <", value, "labelSnapshot");
            return (Criteria) this;
        }

        public Criteria andLabelSnapshotLessThanOrEqualTo(String value) {
            addCriterion("labelSnapshot <=", value, "labelSnapshot");
            return (Criteria) this;
        }

        public Criteria andLabelSnapshotLike(String value) {
            addCriterion("labelSnapshot like", value, "labelSnapshot");
            return (Criteria) this;
        }

        public Criteria andLabelSnapshotNotLike(String value) {
            addCriterion("labelSnapshot not like", value, "labelSnapshot");
            return (Criteria) this;
        }

        public Criteria andLabelSnapshotIn(List<String> values) {
            addCriterion("labelSnapshot in", values, "labelSnapshot");
            return (Criteria) this;
        }

        public Criteria andLabelSnapshotNotIn(List<String> values) {
            addCriterion("labelSnapshot not in", values, "labelSnapshot");
            return (Criteria) this;
        }

        public Criteria andLabelSnapshotBetween(String value1, String value2) {
            addCriterion("labelSnapshot between", value1, value2, "labelSnapshot");
            return (Criteria) this;
        }

        public Criteria andLabelSnapshotNotBetween(String value1, String value2) {
            addCriterion("labelSnapshot not between", value1, value2, "labelSnapshot");
            return (Criteria) this;
        }

        public Criteria andSubmissionidIsNull() {
            addCriterion("submissionid is null");
            return (Criteria) this;
        }

        public Criteria andSubmissionidIsNotNull() {
            addCriterion("submissionid is not null");
            return (Criteria) this;
        }

        public Criteria andSubmissionidEqualTo(String value) {
            addCriterion("submissionid =", value, "submissionid");
            return (Criteria) this;
        }

        public Criteria andSubmissionidNotEqualTo(String value) {
            addCriterion("submissionid <>", value, "submissionid");
            return (Criteria) this;
        }

        public Criteria andSubmissionidGreaterThan(String value) {
            addCriterion("submissionid >", value, "submissionid");
            return (Criteria) this;
        }

        public Criteria andSubmissionidGreaterThanOrEqualTo(String value) {
            addCriterion("submissionid >=", value, "submissionid");
            return (Criteria) this;
        }

        public Criteria andSubmissionidLessThan(String value) {
            addCriterion("submissionid <", value, "submissionid");
            return (Criteria) this;
        }

        public Criteria andSubmissionidLessThanOrEqualTo(String value) {
            addCriterion("submissionid <=", value, "submissionid");
            return (Criteria) this;
        }

        public Criteria andSubmissionidLike(String value) {
            addCriterion("submissionid like", value, "submissionid");
            return (Criteria) this;
        }

        public Criteria andSubmissionidNotLike(String value) {
            addCriterion("submissionid not like", value, "submissionid");
            return (Criteria) this;
        }

        public Criteria andSubmissionidIn(List<String> values) {
            addCriterion("submissionid in", values, "submissionid");
            return (Criteria) this;
        }

        public Criteria andSubmissionidNotIn(List<String> values) {
            addCriterion("submissionid not in", values, "submissionid");
            return (Criteria) this;
        }

        public Criteria andSubmissionidBetween(String value1, String value2) {
            addCriterion("submissionid between", value1, value2, "submissionid");
            return (Criteria) this;
        }

        public Criteria andSubmissionidNotBetween(String value1, String value2) {
            addCriterion("submissionid not between", value1, value2, "submissionid");
            return (Criteria) this;
        }

        public Criteria andNameLikeInsensitive(String value) {
            addCriterion("upper(name) like", value.toUpperCase(), "name");
            return (Criteria) this;
        }

        public Criteria andModelNameLikeInsensitive(String value) {
            addCriterion("upper(modelName) like", value.toUpperCase(), "modelName");
            return (Criteria) this;
        }

        public Criteria andModelParamsLikeInsensitive(String value) {
            addCriterion("upper(modelParams) like", value.toUpperCase(), "modelParams");
            return (Criteria) this;
        }

        public Criteria andStartTimeLikeInsensitive(String value) {
            addCriterion("upper(startTime) like", value.toUpperCase(), "startTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLikeInsensitive(String value) {
            addCriterion("upper(endTime) like", value.toUpperCase(), "endTime");
            return (Criteria) this;
        }

        public Criteria andLabelSnapshotLikeInsensitive(String value) {
            addCriterion("upper(labelSnapshot) like", value.toUpperCase(), "labelSnapshot");
            return (Criteria) this;
        }

        public Criteria andSubmissionidLikeInsensitive(String value) {
            addCriterion("upper(submissionid) like", value.toUpperCase(), "submissionid");
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