/**
 * Created by Zorro on 10/26 026.
 */
package com.hcloud.apm.analysis.controller;

import com.hcloud.apm.analysis.bean.RecommendResult;
import com.hcloud.apm.analysis.bean.TransactionBean;
import com.hcloud.apm.analysis.common.ModelFactory;
import com.hcloud.apm.analysis.common.RecommendModel;
import com.hcloud.apm.analysis.common.SparkInstance;
import com.hcloud.apm.analysis.dao.DatasourceMapper;
import com.hcloud.apm.analysis.dao.TransactionMapper;
import com.hcloud.apm.analysis.domain.Datasource;
import com.hcloud.apm.analysis.domain.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 推荐算法的接口
 */


@RestController
@RequestMapping("/recommend")
public class RecommendController {
    private static final Logger logger = LoggerFactory.getLogger(RecommendController.class);

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private DatasourceMapper datasourceMapper;
    /**
     * 基于用户的推荐
     * @param transactionId  事务Id
     * @param userId         用户Id
     * @param resultNum      推荐结果数
     * @return
     */
    @GetMapping("/{transactionId}/user")
    public List<RecommendResult> recommendByUser(@PathVariable int transactionId,
                                                 @RequestParam String userId,
                                                 @RequestParam int resultNum) {
        logger.info("GET /recommend/{}/user?userId={}&resultNum={}", transactionId, userId, resultNum);
        Transaction transaction = transactionMapper.selectByPrimaryKey(transactionId);
        Datasource datasource = datasourceMapper.selectByPrimaryKey(transaction.getDataSourceid());
        RecommendModel recommendModel = ModelFactory.getRecommendModel(transaction.getModelName());
        recommendModel.spark_$eq(SparkInstance.spark());
        TransactionBean bean = new TransactionBean(transaction, datasource);
        // return JavaConversions.seqAsJavaList(recommendModel.recommendByUser(bean, userId, resultNum));
        return null;
    }


    /**
     * 基于物品相似度的推荐
     * @param transactionId  事务Id
     * @param productId      物品Id
     * @param resultNum      推荐结果数
     * @return
     */
    @GetMapping("/{transactionId}/product")
    public List<RecommendResult> recommendByProduct(@PathVariable int transactionId,
                                                    @RequestParam String productId,
                                                    @RequestParam int resultNum) {
        logger.info("GET /recommend/{}/product?productId={}&resultNum={}", transactionId, productId, resultNum);
        Transaction transaction = transactionMapper.selectByPrimaryKey(transactionId);
        Datasource datasource = datasourceMapper.selectByPrimaryKey(transaction.getDataSourceid());
        RecommendModel recommendModel = ModelFactory.getRecommendModel(transaction.getModelName());
        recommendModel.spark_$eq(SparkInstance.spark());
        TransactionBean bean = new TransactionBean(transaction, datasource);
        // return JavaConversions.seqAsJavaList(recommendModel.recommendByProduct(bean, productId, resultNum));
        return null;
    }
}
