/**
 * Created by Zorro on 10/31 031.
 */
package com.hcloud.apm.analysis.exception;

/**
 * 自定义异常类
 */
public class APMException extends Exception {
    Object detail;

    public Object getDetail() {
        return detail;
    }

    public void setDetail(Object detail) {
        this.detail = detail;
    }

    public APMException(String message, Object detail) {
        super(message);
        this.detail = detail;
    }
}
