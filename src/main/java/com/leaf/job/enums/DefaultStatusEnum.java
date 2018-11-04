package com.leaf.job.enums;

import com.leaf.job.utility.CommonConstant;

public enum DefaultStatusEnum {
	
    ACTIVE(CommonConstant.DEFAULT_STATUS_ACTIVE),
    DEACTIVE(CommonConstant.DEFAULT_STATUS_DEACTIVE);


    private String code;

    DefaultStatusEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static DefaultStatusEnum getEnum(String code){
        switch (code){
            case CommonConstant.DEFAULT_STATUS_ACTIVE:
                return ACTIVE;
            default:
                return DEACTIVE;
        }
    }
}
