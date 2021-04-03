package com.leaf.job.dto;

public class StudentExaminationDTO {
    private Long id;
    private String student;
    private String examinationCode;
    private String examinationDescription;
    private Integer noQuestion;
    private String duration;
    private String statusCode;
    private String statusDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getExaminationCode() {
        return examinationCode;
    }

    public void setExaminationCode(String examinationCode) {
        this.examinationCode = examinationCode;
    }

    public String getExaminationDescription() {
        return examinationDescription;
    }

    public void setExaminationDescription(String examinationDescription) {
        this.examinationDescription = examinationDescription;
    }

    public Integer getNoQuestion() {
        return noQuestion;
    }

    public void setNoQuestion(Integer noQuestion) {
        this.noQuestion = noQuestion;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }
}
