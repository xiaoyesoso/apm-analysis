/**
 * Created by Zorro on 10/21 021.
 */
package com.hcloud.apm.analysis.controller;

import com.hcloud.apm.analysis.bean.TransactionBean;
import com.hcloud.apm.analysis.common.TransactionManagement;
import com.hcloud.apm.analysis.dao.DatasourceMapper;
import com.hcloud.apm.analysis.dao.TransactionMapper;
import com.hcloud.apm.analysis.domain.Datasource;
import com.hcloud.apm.analysis.domain.Transaction;
import com.hcloud.apm.analysis.domain.TransactionExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
   private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

   @Autowired
   private TransactionMapper transactionMapper;

   @Autowired
   private DatasourceMapper datasourceMapper;

   @Autowired
   private TransactionManagement transactionManagement;

   /**
    * 获取事务列表
    * @return List<Transaction>
    */
   @GetMapping("")
   public List<Transaction> list() {
       logger.info("GET /transaction");
       return transactionMapper.selectByExample(new TransactionExample());
   }

   /**
    * 获取事务信息
    * @param id
    * @return
    */
   @GetMapping("/{id}")
   public Transaction get(@PathVariable int id) {
       logger.info("GET /transaction/{}", id);
       return transactionMapper.selectByPrimaryKey(id);
   }

   /**
    * 创建/更新事务
    * @param transaction Transaction实体(id为空时为创建)
    */
   @PutMapping(value = "")
   public int createOrUpdate(@RequestBody Transaction transaction) {
       logger.info("PUT /transaction {}", transaction);
       if (transaction.getId() == null || transaction.getId() == 0) {
           transactionMapper.insert(transaction);
       } else {
           transactionMapper.updateByPrimaryKey(transaction);
       }
       return transaction.getId();
   }

   /**
    * 删除事务
    * @param id
    * @return
    */
   @DeleteMapping("/{id}")
   public int delete(@PathVariable int id) {
       logger.info("DELETE /transaction/{}", id);
       transactionMapper.deleteByPrimaryKey(id);
       return id;
   }

   /**
    * 开始执行事务
    * @param id
    * @return
    */
   @PostMapping("/start/{id}")
   @ResponseBody
   public Map<String, Object> start(@PathVariable int id) {
       Map<String, Object> map = new HashMap<String, Object>();

       map.put("success", true);
       logger.info("POST /transaction/start/{}", id);
       try{
           Transaction transaction = transactionMapper.selectByPrimaryKey(id);

           Datasource datasource = datasourceMapper.selectByPrimaryKey(transaction.getDataSourceid());
            transactionManagement.startTransaction(new TransactionBean(transaction, datasource));

       }catch (Exception e){
           map.put("success", false);
       }
       return map;
   }

   /**
    * 终止执行事务
    * @param id
    * @return
    */
   @PostMapping("/stop/{id}")
   public int stop(@PathVariable int id) {
       logger.info("POST /transaction/stop/{}", id);
       transactionManagement.stopTransaction(id);
       return id;
   }


}
