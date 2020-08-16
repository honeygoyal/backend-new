package com.egatetutor.backend.model.responsemodel;

import com.egatetutor.backend.model.UserInfo;

import javax.persistence.Column;

public class ReportOverallRequest {

    @Column(name = "total_time")
    private String totalTime;

    private String status;

    private Long userId;
    private Long courseId;

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
