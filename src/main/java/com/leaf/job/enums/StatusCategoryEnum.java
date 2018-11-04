package com.leaf.job.enums;

import com.leaf.job.utility.CommonConstant;

public enum StatusCategoryEnum {
	
	DEFAULT(CommonConstant.STATUS_CATEGORY_DEFAULT),
    DELETE(CommonConstant.STATUS_CATEGORY_DELETE);


    private String code;

    StatusCategoryEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static StatusCategoryEnum getEnum(String code){
        switch (code){
            case CommonConstant.STATUS_CATEGORY_DEFAULT:
                return DEFAULT;
            default:
                return DELETE;
        }
}
}
