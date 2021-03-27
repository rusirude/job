package com.leaf.job.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.leaf.job.utility.CommonConstant;

import java.util.Date;

public class StudentDTO extends SysUserDTO{

    @JsonFormat(pattern = CommonConstant.SYSTEM_DATE_FORMAT, timezone = CommonConstant.DATE_TIMEZONE_JACKSON)
    private Date dob;
    private String email;
    private String telephone;
    @JsonFormat(pattern = CommonConstant.SYSTEM_DATE_FORMAT, timezone = CommonConstant.DATE_TIMEZONE_JACKSON)
    private Date effectiveOn;
    @JsonFormat(pattern = CommonConstant.SYSTEM_DATE_FORMAT, timezone = CommonConstant.DATE_TIMEZONE_JACKSON)
    private Date expireOn;
    private String examCode;


    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

    public String getExamCode() {
        return examCode;
    }

    public void setExamCode(String examCode) {
        this.examCode = examCode;
    }
}