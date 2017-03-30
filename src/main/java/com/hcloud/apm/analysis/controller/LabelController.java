package com.hcloud.apm.analysis.controller;

import com.hcloud.apm.analysis.bean.TransactionBean;
import com.hcloud.apm.analysis.common.ModelFactory;
import com.hcloud.apm.analysis.common.SparkInstance;
import com.hcloud.apm.analysis.dao.DatasourceMapper;
import com.hcloud.apm.analysis.dao.TransactionMapper;
import com.hcloud.apm.analysis.domain.Datasource;
import com.hcloud.apm.analysis.domain.Transaction;
import com.hcloud.apm.analysis.label.BasicLabelModel;
import com.hcloud.apm.analysis.label.KMeansLabelModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/label")
public class LabelController {
    private static final Logger logger = LoggerFactory.getLogger(LabelController.class);
    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private DatasourceMapper datasourceMapper;

    @GetMapping("/basic/{transactionId}")
    @ResponseBody
    public Map<String,Object> basicLabelByTransaction(@PathVariable int transactionId) {
        logger.info("GET /label/{}/", transactionId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", true);
        try{
            Transaction transaction = transactionMapper.selectByPrimaryKey(transactionId);
            Datasource datasource = datasourceMapper.selectByPrimaryKey(transaction.getDataSourceid());
            BasicLabelModel labelModel = ModelFactory.getBasicLabelModel("BasicLabelModel");
             labelModel.spark_$eq(SparkInstance.spark());
            TransactionBean bean = new TransactionBean(transaction, datasource);
            labelModel.fit(bean);
        }catch (Exception e){
            map.put("success", false);
        }


        return map;
    }

    @GetMapping("/kmeans/{transactionId}")
    @ResponseBody
    public Map<String,Object> kMeansLabelByTransaction(@PathVariable int transactionId) {
        logger.info("GET /label/{}/", transactionId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", true);
        try{
            Transaction transaction = transactionMapper.selectByPrimaryKey(transactionId);
            Datasource datasource = datasourceMapper.selectByPrimaryKey(transaction.getDataSourceid());
            KMeansLabelModel kMeansModel = ModelFactory.getKMeansLabelModel("KMeansLabelModel");
            kMeansModel.spark_$eq(SparkInstance.spark());
            TransactionBean bean = new TransactionBean(transaction, datasource);
            kMeansModel.fit(bean);
        }catch (Exception e){
            map.put("success", false);
        }

        return map;



    }




}
