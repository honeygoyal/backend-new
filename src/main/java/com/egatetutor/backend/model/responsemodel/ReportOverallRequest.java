package com.egatetutor.backend.model.responsemodel;

import com.egatetutor.backend.model.UserInfo;

import javax.persistence.Column;

public class ReportOverallRequest {

    @Column(name = "total_time")
    private String totalTime;

    @Column(name = "score")
    private Double score;

    @Column(name = "user_rank")
    private int userRank;

    private String status;

    private Long userId;
    private Long courseId;

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public int getUserRank() {
        return userRank;
    }

    public void setUserRank(int userRank) {
        this.userRank = userRank;
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
