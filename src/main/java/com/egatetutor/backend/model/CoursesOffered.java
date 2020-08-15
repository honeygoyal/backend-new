package com.egatetutor.backend.model;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "courses_offered", indexes = @Index(name = "exam_id", columnList = "exam_id", unique = true))
public class CoursesOffered implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 3230408103166494796L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "exam_name")
    private String examName;

    @Column(name = "exam_name_service")
    private String examNameService;

    @Column(name = "price")
    private Double price;

    @Column(name = "branch")
    private String branch;

    @Column(name = "exam_id", nullable=false)
    private String examId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getExamNameService() {
        return examNameService;
    }

    public void setExamNameService(String examNameService) {
        this.examNameService = examNameService;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examCode) {
        this.examId = examCode;
    }
}
