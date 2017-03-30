/**
 * Created by Zorro on 10/20 020.
 */
package com.hcloud.apm.analysis.controller;

import com.hcloud.apm.analysis.common.DataSourceValidator;
import com.hcloud.apm.analysis.dao.DatasourceMapper;
import com.hcloud.apm.analysis.domain.Datasource;
import com.hcloud.apm.analysis.domain.DatasourceExample;
import com.hcloud.apm.analysis.exception.APMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据源
 */


@RestController
@RequestMapping("/dataSource")
public class DataSourceController {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceController.class);

    @Autowired
    private DatasourceMapper datasourceMapper;

    @Autowired
    private DataSourceValidator validator;

    /**
     * 获取数据源列表
     * @return List<Datasource>
     */
    @GetMapping("")
    public List<Datasource> list() {
        logger.info("GET /dataSource");
        return datasourceMapper.selectByExample(new DatasourceExample());
    }

    /**
     * 获取数据源信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Datasource get(@PathVariable int id) {
        logger.info("GET /dataSource/{}", id);
        return datasourceMapper.selectByPrimaryKey(id);
    }

    /**
     * 创建/更新数据源
     * @param dataSource Datasource实体(id为空时为创建)
     */
    @PutMapping(value = "")
    public int createOrUpdate(@RequestBody Datasource dataSource,
                              Boolean validate) throws APMException {
        logger.info("PUT /dataSource {}", dataSource);
        if (validate == null) validate = Boolean.TRUE;
        if (validate) {
            scala.Tuple2<Integer, List<String>> result = validator.validateDataSource(dataSource);
            if (result._1 > 0)
                throw new APMException("数据源校验失败！共有" + result._1 + "处错误！(只显示前10条)", result._2);
        }
        if (dataSource.getId() == null || dataSource.getId() == 0) {
            datasourceMapper.insert(dataSource);
        } else {
            datasourceMapper.updateByPrimaryKey(dataSource);
        }
        return dataSource.getId();
    }

    /**
     * 删除数据源
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public int delete(@PathVariable int id) {
        logger.info("DELETE /dataSource/{}", id);
        datasourceMapper.deleteByPrimaryKey(id);
        return id;
    }
}
