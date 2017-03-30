package com.hcloud.apm.analysis.dao;

import com.hcloud.apm.analysis.domain.Datasource;
import com.hcloud.apm.analysis.domain.DatasourceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

//@Repository
public interface DatasourceMapper {
    long countByExample(DatasourceExample example);

    int deleteByExample(DatasourceExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Datasource record);

    int insertSelective(Datasource record);

    List<Datasource> selectByExample(DatasourceExample example);

    Datasource selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Datasource record, @Param("example") DatasourceExample example);

    int updateByExample(@Param("record") Datasource record, @Param("example") DatasourceExample example);

    int updateByPrimaryKeySelective(Datasource record);

    int updateByPrimaryKey(Datasource record);
}