package com.hcloud.apm.analysis.domain;

import java.io.Serializable;

public class Transaction implements Serializable {
    private Integer id;

    private String name;

    private String modelName;

    private String modelParams;

    private String startTime;

    private String endTime;

    private Integer status;

    private Integer dataSourceid;

    private String labelSnapshot;

    private String submissionid;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName == null ? null : modelName.trim();
    }

    public String getModelParams() {
        return modelParams;
    }

    public void setModelParams(String modelParams) {
        this.modelParams = modelParams == null ? null : modelParams.trim();
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime == null ? null : startTime.trim();
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime == null ? null : endTime.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDataSourceid() {
        return dataSourceid;
    }

    public void setDataSourceid(Integer dataSourceid) {
        this.dataSourceid = dataSourceid;
    }

    public String getLabelSnapshot() {
        return labelSnapshot;
    }

    public void setLabelSnapshot(String labelSnapshot) {
        this.labelSnapshot = labelSnapshot == null ? null : labelSnapshot.trim();
    }

    public String getSubmissionid() {
        return submissionid;
    }

    public void setSubmissionid(String submissionid) {
        this.submissionid = submissionid == null ? null : submissionid.trim();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Transaction other = (Transaction) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getModelName() == null ? other.getModelName() == null : this.getModelName().equals(other.getModelName()))
            && (this.getModelParams() == null ? other.getModelParams() == null : this.getModelParams().equals(other.getModelParams()))
            && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()))
            && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getDataSourceid() == null ? other.getDataSourceid() == null : this.getDataSourceid().equals(other.getDataSourceid()))
            && (this.getLabelSnapshot() == null ? other.getLabelSnapshot() == null : this.getLabelSnapshot().equals(other.getLabelSnapshot()))
            && (this.getSubmissionid() == null ? other.getSubmissionid() == null : this.getSubmissionid().equals(other.getSubmissionid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getModelName() == null) ? 0 : getModelName().hashCode());
        result = prime * result + ((getModelParams() == null) ? 0 : getModelParams().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getDataSourceid() == null) ? 0 : getDataSourceid().hashCode());
        result = prime * result + ((getLabelSnapshot() == null) ? 0 : getLabelSnapshot().hashCode());
        result = prime * result + ((getSubmissionid() == null) ? 0 : getSubmissionid().hashCode());
        return result;
    }
}