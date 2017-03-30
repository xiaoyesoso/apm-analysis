/**
 * Created by Zorro on 10/26 026.
 */
package com.hcloud.apm.analysis.bean;

import java.io.Serializable;

/**
 * 推荐结果
 */
public class RecommendResult implements Serializable {
    String productId;
    double rating;

    public RecommendResult() {
    }

    public RecommendResult(String productId, double rating) {
        this.productId = productId;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "RecommendResult{" +
                "productId='" + productId + '\'' +
                ", rating=" + rating +
                '}';
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
