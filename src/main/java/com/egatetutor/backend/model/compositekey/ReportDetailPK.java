package com.egatetutor.backend.model.compositekey;

import com.egatetutor.backend.model.ReportOverall;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ReportDetailPK implements Serializable
{
    @Column(name = "report_id")
    private ReportOverallPK reportId;

    @Column(name = "question_id")
    private long questionId;

    public ReportDetailPK() {
    }

    public ReportDetailPK(ReportOverallPK reportId, long questionId) {
        this.reportId = reportId;
        this.questionId = questionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportDetailPK)) return false;
        ReportDetailPK that = (ReportDetailPK) o;
        return getQuestionId() == that.getQuestionId() &&
                getReportId().equals(that.getReportId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReportId(), getQuestionId());
    }

    public ReportOverallPK getReportId() {
        return reportId;
    }

    public void setReportId(ReportOverallPK reportId) {
        this.reportId = reportId;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

}
