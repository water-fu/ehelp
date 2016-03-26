package com.wisdom.mapp.core.exception;

/**
 *
 */
public  class ExceptionCode {

	private String key;
	
	private String value;
	
	
	protected ExceptionCode(String key) {
		this.key = key;
	}
	
	protected ExceptionCode(String key,String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}



	public static ExceptionCode EX_CORE_UNKNOW=new ExceptionCode("exception.core.unknown");


	
}
