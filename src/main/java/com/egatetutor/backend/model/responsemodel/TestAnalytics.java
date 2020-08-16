package com.egatetutor.backend.model.responsemodel;

public class TestAnalytics {
    private int correct;
    private int inCorrect;
    private int unAttempt;
    private double MarksSecured;
    private double totalMarks;

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

    public double getMarksSecured() {
        return MarksSecured;
    }

    public void setMarksSecured(double marksSecured) {
        MarksSecured = marksSecured;
    }

    public double getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(double totalMarks) {
        this.totalMarks = totalMarks;
    }
}
