/**
 * Created by Zorro on 10/21 021.
 */
package com.hcloud.apm.analysis.bean;

import com.hcloud.apm.analysis.domain.Datasource;
import com.hcloud.apm.analysis.domain.Transaction;

/**
 * 用于传递给算法模型的事务参数
 */
public class TransactionBean extends Transaction {

    private DataSourceBean datasource;

    public DataSourceBean getDatasource() {
        return datasource;
    }

    public void setDatasource(Datasource datasource) {
        this.datasource = new DataSourceBean(datasource);
    }

    public TransactionBean() {}

    public TransactionBean(Transaction transaction, Datasource datasource) {
        setId(transaction.getId());
        setName(transaction.getName());
        setModelName(transaction.getModelName());
        setModelParams(transaction.getModelParams());
        setDataSourceid(transaction.getDataSourceid());
        setLabelSnapshot(transaction.getLabelSnapshot());
        setStatus(transaction.getStatus());
        setDatasource(datasource);
        setStartTime(transaction.getStartTime());
    }
}
