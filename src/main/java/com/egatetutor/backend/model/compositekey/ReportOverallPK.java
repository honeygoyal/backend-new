package com.egatetutor.backend.model.compositekey;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ReportOverallPK implements Serializable {
    @Column(name = "course_id")
    private long courseId;

    @Column(name = "user_id")
    private long userId;

    public ReportOverallPK() {
    }

    public ReportOverallPK(long courseId, long userId) {
        this.courseId = courseId;
        this.userId = userId;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportOverallPK)) return false;
        ReportOverallPK that = (ReportOverallPK) o;
        return courseId == that.courseId &&
                userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, userId);
    }
}
