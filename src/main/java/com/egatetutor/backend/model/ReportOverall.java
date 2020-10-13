package com.egatetutor.backend.model;
import com.egatetutor.backend.model.compositekey.ReportOverallPK;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "report_overall")
public class ReportOverall implements Serializable {


    @AttributeOverrides(value =
            {@AttributeOverride(column = @Column(name="course_id"), name = "course_id"),
                    @AttributeOverride(column = @Column(name="user_id"), name = "user_id")
            })
    @EmbeddedId
    private ReportOverallPK reportOverallPK;

    @MapsId("course_id")
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "course_id", referencedColumnName = "id", nullable = false)
    private CoursesDescription courseId;

    @MapsId("user_id")
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserInfo userId;

    @Column(name = "total_time")
    private String totalTime;

    @Column(name = "score")
    private Double score;

    @Column(name = "user_rank")
    private int userRank;
    private String status;
    private int correct;
    private int inCorrect;
    private int unAttempt;

    public CoursesDescription getCourseId() {
        return courseId;
    }

    public void setCourseId(CoursesDescription courseId) {
        this.courseId = courseId;
    }

    public UserInfo getUserId() {
        return userId;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getInCorrect() {
        return inCorrect;
    }

    public void setInCorrect(int inCorrect) {
        this.inCorrect = inCorrect;
    }

    public int getUnAttempt() {
        return unAttempt;
    }

    public void setUnAttempt(int unAttempt) {
        this.unAttempt = unAttempt;
    }

    public void setUserId(UserInfo userId) {
        this.userId = userId;
    }

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

    public ReportOverallPK getReportOverallPK() {
        return reportOverallPK;
    }

    public void setReportOverallPK(ReportOverallPK reportOverallPK) {
        this.reportOverallPK = reportOverallPK;
    }
}
