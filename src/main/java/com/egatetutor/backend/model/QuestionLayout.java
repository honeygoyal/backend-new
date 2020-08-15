package com.egatetutor.backend.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "question_layout", indexes = @Index(name = "question_id", columnList = "course_id,question_label,section", unique = true))
public class QuestionLayout implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",  nullable = false, updatable = false)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="course_id",referencedColumnName = "id")
    private CoursesDescription courseId;

    @Column(name = "section", nullable=false)
    private String section;

    private double marks;

    @Column(name = "negative_marks")
    private double negativeMarks;

    private String questionType;

    private String question;

    private String answer;

    private String solution;

    @Column(name = "question_difficulty")
    private String questionDifficulty;

    @Column(name = "video_link")
    private String videoLink;

    @Column(name = "question_label", nullable=false)
    private int questionLabel;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CoursesDescription getCourseId() {
        return courseId;
    }

    public void setCourseId(CoursesDescription courseId) {
        this.courseId = courseId;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    public double getNegativeMarks() {
        return negativeMarks;
    }

    public void setNegativeMarks(double negativeMarks) {
        this.negativeMarks = negativeMarks;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getQuestionDifficulty() {
        return questionDifficulty;
    }

    public void setQuestionDifficulty(String questionDifficulty) {
        this.questionDifficulty = questionDifficulty;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public int getQuestionLabel() {
        return questionLabel;
    }

    public void setQuestionLabel(int questionLabel) {
        this.questionLabel = questionLabel;
    }
}
