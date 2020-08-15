package com.egatetutor.backend.model.responsemodel;

import com.egatetutor.backend.model.QuestionLayout;

import javax.persistence.Column;

public class ReportDetailRequest {


    @Column(name = "question_id")
    private Long questionId;


    @Column(name = "user_id")
    private Long userId;

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "answer_submitted")
    private String answerSubmitted;

    @Column(name = "question_status")
    private String questionStatus;

    @Column(name = "time_taken")
    private String timeTaken;

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
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

    public String getAnswerSubmitted() {
        return answerSubmitted;
    }

    public void setAnswerSubmitted(String answerSubmitted) {
        this.answerSubmitted = answerSubmitted;
    }

    public String getQuestionStatus() {
        return questionStatus;
    }

    public void setQuestionStatus(String questionStatus) {
        this.questionStatus = questionStatus;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }
}
