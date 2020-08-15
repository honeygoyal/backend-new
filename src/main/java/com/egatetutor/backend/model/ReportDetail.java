package com.egatetutor.backend.model;

import com.egatetutor.backend.model.compositekey.ReportDetailPK;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "report_detail")
public class ReportDetail implements Serializable {


    @AttributeOverrides(value =
            {@AttributeOverride(column = @Column(name="report_id"), name = "report_id"),
                    @AttributeOverride(column = @Column(name="question_id"), name = "question_id")
                    })
    @EmbeddedId
    private ReportDetailPK reportDetailPK;

    @MapsId("report_id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumns({
            @JoinColumn(name="course_id", referencedColumnName="course_id"),
            @JoinColumn(name="user_id", referencedColumnName="user_id")
    })
     private ReportOverall reportId;

    @MapsId("question_id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "question_id", referencedColumnName = "id", nullable = false)
    private QuestionLayout question_id;

    @Column(name = "answer_submitted")
    private String answerSubmitted;

    @Column(name = "question_status")
    private String questionStatus;

    @Column(name = "time_taken")
    private String timeTaken;

    public ReportOverall getReportId() {
        return reportId;
    }

    public void setReportId(ReportOverall reportId) {
        this.reportId = reportId;
    }

    public QuestionLayout getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(QuestionLayout question_id) {
        this.question_id = question_id;
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

    public ReportDetailPK getReportDetailPK() {
        return reportDetailPK;
    }

    public void setReportDetailPK(ReportDetailPK reportDetailPK) {
        this.reportDetailPK = reportDetailPK;
    }
}
