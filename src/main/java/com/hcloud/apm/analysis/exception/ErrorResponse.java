/**
 * Created by Zorro on 10/25 025.
 */
package com.hcloud.apm.analysis.exception;

/**
 * 异常时返回的信息
 */
public class ErrorResponse {
    private String message;
    private Object detail;

    public ErrorResponse(Throwable ex) {
        this.message = ex.getMessage();
    }

    public Object getDetail() {
        return detail;
    }

    public void setDetail(Object detail) {
        this.detail = detail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
