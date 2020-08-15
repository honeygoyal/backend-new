package com.egatetutor.backend.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "subscription_details", indexes = @Index(name = "sid", columnList = "user_id,course_offered_id", unique = true))
public class SubscriptionDetails implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",  nullable = false, updatable = false)
    private long id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserInfo userId;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "course_offered_id", referencedColumnName = "id", nullable = false)
    private CoursesOffered courseOfferedId;

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

    public CoursesOffered getCourseOfferedId() {
        return courseOfferedId;
    }

    public void setCourseOfferedId(CoursesOffered courseOfferedId) {
        this.courseOfferedId = courseOfferedId;
    }
}
