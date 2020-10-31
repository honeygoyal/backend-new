package com.egatetutor.backend.model.responsemodel;

import java.util.List;

public class PurchaseCourse {
    String paymentId;
    String orderId;
    String signature;
    Long userId;
    List<String> examId;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<String> getExamId() {
        return examId;
    }

    public void setExamId(List<String> examId) {
        this.examId = examId;
    }
}
