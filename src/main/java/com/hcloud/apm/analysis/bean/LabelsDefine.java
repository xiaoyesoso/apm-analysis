package com.hcloud.apm.analysis.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈志民 on 2016/11/23.
 */
public class LabelsDefine{
    ArrayList<LabelBeanJava> labels;

    public ArrayList<LabelBeanJava> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<LabelBeanJava> labels) {
        this.labels = labels;
    }
}


class LabelBeanJava{
    String labelName;
    List<FieldJava> fields;

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public List<FieldJava> getFields() {
        return fields;
    }

    public void setFields(List<FieldJava> fields) {
        this.fields = fields;
    }
}

class  FieldJava{
    String fieldName;
    String operation;
    String param;
    String relation;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}


