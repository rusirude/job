package com.leaf.job.dto.common;

public class ResponseDTO<T> {
	
	private String code;
	private String messsage;
	private T data;	
	
	public ResponseDTO(String code) {		
		this.code = code;
	}
	
	public ResponseDTO(String code, String messsage) {		
		this.code = code;
		this.messsage = messsage;
	}

	public ResponseDTO(String code, String messsage, T data) {		
		this.code = code;
		this.messsage = messsage;
		this.data = data;
	}

	public ResponseDTO(String code, T data) {
		this.code = code;
		this.data = data;
	}


	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMesssage() {
		return messsage;
	}
	public void setMesssage(String messsage) {
		this.messsage = messsage;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
	
	
}
