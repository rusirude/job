package com.leaf.job.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.leaf.job.dto.common.CommonDTO;
import com.leaf.job.utility.CommonConstant;

import java.util.Date;

public class ExaminationDTO extends CommonDTO {

    private String code;
    private String description;
    private String questionCategoryCode;
    private String questionCategoryDescription;
    private String statusCode;
    private String statusDescription;
    @JsonFormat(pattern = CommonConstant.SYSTEM_DATE_TIME_FORMAT, timezone = CommonConstant.DATE_TIMEZONE_JACKSON)
    private Date effectiveOn;
    @JsonFormat(pattern = CommonConstant.SYSTEM_DATE_TIME_FORMAT, timezone = CommonConstant.DATE_TIMEZONE_JACKSON)
    private Date expireOn;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuestionCategoryCode() {
        return questionCategoryCode;
    }

    public void setQuestionCategoryCode(String questionCategoryCode) {
        this.questionCategoryCode = questionCategoryCode;
    }

    public String getQuestionCategoryDescription() {
        return questionCategoryDescription;
    }

    public void setQuestionCategoryDescription(String questionCategoryDescription) {
        this.questionCategoryDescription = questionCategoryDescription;
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

    public Date getEffectiveOn() {
        return effectiveOn;
    }

    public void setEffectiveOn(Date effectiveOn) {
        this.effectiveOn = effectiveOn;
    }

    public Date getExpireOn() {
        return expireOn;
    }

    public void setExpireOn(Date expireOn) {
        this.expireOn = expireOn;
    }
}