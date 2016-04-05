package com.wisdom.mapp.core.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.xml.bind.annotation.XmlSeeAlso;

@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
@XmlSeeAlso({MappHeader.class})
public interface IHeader  {

	public String getBizCode();
	
	public String getIdentityId();
	
	public void setRespCode(String respCode);
	
	public void setRespMsg(String respMsg);
	
	public String getRespCode();
	
	public String getRespMsg();
	
	public String getSign();
	
	public String getMode();
	
	public void setSign(String sign);
	
}
