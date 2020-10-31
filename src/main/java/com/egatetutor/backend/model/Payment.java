package com.egatetutor.backend.model;

import javax.persistence.*;

@Entity
@Table(name = "payment", indexes = @Index(name = "paymentId", columnList = "user_id,order_id", unique = true))
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",  nullable = false, updatable = false)
    private long id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserInfo userId;

    @Column(name = "order_id", nullable = false, updatable = false)
    private String orderId;

    @Column(name = "course_id")
    private String courseId;

    @Column(name = "payment_id")
    private String paymentId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserInfo getUserId() {
        return userId;
    }

    public void setUserId(UserInfo userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}
