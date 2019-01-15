package com.leaf.job.enums;

public enum MasterDataEnum {
	DEFAULT_PASSWORD("DEFAULT_PASSWORD"),
	COMPANY_NAME("COMPANY_NAME");
	
	private String code;
	
	MasterDataEnum(String code) {
        this.code = code;
    }
	
	public String getCode() {
		return this.code;
	}
	
    public static MasterDataEnum getEnum(String code){
        switch (code){
        	case "COMPANY_NAME":
        		return COMPANY_NAME;
            default:
                return DEFAULT_PASSWORD;
        }
    }
	
}
