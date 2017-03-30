package com.hcloud.apm.analysis.domain;

import java.io.Serializable;

public class Datasource implements Serializable {
    private Integer id;

    private String name;

    private String info;

    private String time;

    private Integer type;

    private String fieldSchema;

    private String fileLocation;

    private Integer deleted;

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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info == null ? null : info.trim();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time == null ? null : time.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getFieldSchema() {
        return fieldSchema;
    }

    public void setFieldSchema(String fieldSchema) {
        this.fieldSchema = fieldSchema == null ? null : fieldSchema.trim();
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation == null ? null : fileLocation.trim();
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
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
        Datasource other = (Datasource) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getInfo() == null ? other.getInfo() == null : this.getInfo().equals(other.getInfo()))
            && (this.getTime() == null ? other.getTime() == null : this.getTime().equals(other.getTime()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getFieldSchema() == null ? other.getFieldSchema() == null : this.getFieldSchema().equals(other.getFieldSchema()))
            && (this.getFileLocation() == null ? other.getFileLocation() == null : this.getFileLocation().equals(other.getFileLocation()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getInfo() == null) ? 0 : getInfo().hashCode());
        result = prime * result + ((getTime() == null) ? 0 : getTime().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getFieldSchema() == null) ? 0 : getFieldSchema().hashCode());
        result = prime * result + ((getFileLocation() == null) ? 0 : getFileLocation().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        return result;
    }
}