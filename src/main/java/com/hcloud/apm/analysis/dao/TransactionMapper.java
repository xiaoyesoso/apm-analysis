package com.hcloud.apm.analysis.dao;

import com.hcloud.apm.analysis.domain.Transaction;
import com.hcloud.apm.analysis.domain.TransactionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

//@Repository
public interface TransactionMapper {
    long countByExample(TransactionExample example);

    int deleteByExample(TransactionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Transaction record);

    int insertSelective(Transaction record);

    List<Transaction> selectByExample(TransactionExample example);

    Transaction selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Transaction record, @Param("example") TransactionExample example);

    int updateByExample(@Param("record") Transaction record, @Param("example") TransactionExample example);

    int updateByPrimaryKeySelective(Transaction record);

    int updateByPrimaryKey(Transaction record);
}