/**
 * Created by Zorro on 11/1 001.
 */
package com.hcloud.apm.analysis.bean;

import com.alibaba.fastjson.JSON;
import com.hcloud.apm.analysis.domain.Datasource;

/**
 * 数据源实体
 */
public class DataSourceBean extends Datasource {
    /**
     * 数据源的数据结构表示
     */
    private DataSourceSchema schema;

    public DataSourceBean(Datasource source) {
        setId(source.getId());
        setFileLocation(source.getFileLocation());
        setName(source.getName());
        setType(source.getType());
        setFieldSchema(source.getFieldSchema());
        setSchema(JSON.parseObject(source.getFieldSchema(), DataSourceSchema.class));
    }

    public DataSourceSchema getSchema() {
        return schema;
    }

    public void setSchema(DataSourceSchema schema) {
        this.schema = schema;
        // 分隔符默认为空
        if (schema.getSeparator() == null || schema.getSeparator().isEmpty()) {
            schema.setSeparator(" ");
        }
    }
}
