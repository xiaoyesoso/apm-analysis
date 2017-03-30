/**
 * Created by Zorro on 11/1 001.
 */
package com.hcloud.apm.analysis.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 数据源的数据结构
 */
public class DataSourceSchema implements Serializable {

    /**
     * 数据源的字段
     */
    private List<Field> fields;

    /**
     * 分隔符
     */
    private String separator;

    /**
     * 是否删除首行
     */
    private boolean deleteFirstLine;

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public boolean isDeleteFirstLine() {
        return deleteFirstLine;
    }

    public void setDeleteFirstLine(boolean deleteFirstLine) {
        this.deleteFirstLine = deleteFirstLine;
    }

    /**
     * 字段
     */
    public static class Field implements Serializable {
        private String fieldName;
        private String fieldType;
        private Boolean nullable;
        private Boolean isKey;

        public Boolean getIsKey() {
            return isKey;
        }

        public void setIsKey(Boolean key) {
            isKey = key;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }


        public String getFieldType() {
            return fieldType;
        }

        public void setFieldType(String fieldType) {
            this.fieldType = fieldType;
        }

        public Boolean getNullable() {
            return nullable;
        }

        public void setNullable(Boolean nullable) {
            this.nullable = nullable;
        }

    }
}