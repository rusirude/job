package com.leaf.job.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.leaf.job.utility.CommonConstant;

import java.util.Date;

public class ExamQuestionDTO {
    private QuestionDTO question;
    @JsonFormat(pattern = CommonConstant.SYSTEM_DATE_TIME_FORMAT, timezone = CommonConstant.DATE_TIMEZONE_JACKSON)
    private Date startTime;
    @JsonFormat(pattern = CommonConstant.SYSTEM_DATE_TIME_FORMAT, timezone = CommonConstant.DATE_TIMEZONE_JACKSON)
    private Date currentTime;
    private String duration;
    private Integer total;

    public QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDTO question) {
        this.question = question;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
