package com.leaf.job.enums;

import com.leaf.job.utility.CommonConstant;

public enum ExamStatusEnum {

    PENDING(CommonConstant.EXAM_STATUS_PENDING),
    START(CommonConstant.EXAM_STATUS_START);


    private String code;

    ExamStatusEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static ExamStatusEnum getEnum(String code){
        switch (code){
            case CommonConstant.EXAM_STATUS_START:
                return START;
            default:
                return PENDING;
        }
    }
}
