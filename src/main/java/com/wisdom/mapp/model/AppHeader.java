package com.wisdom.mapp.model;


import com.wisdom.mapp.core.model.IHeader;

/**
 *
 * Created by fusj on 16/1/15.
 */
public class AppHeader implements IHeader {

	private String bizCode;
	
	private String identityId;
	
	private String coordinates;
	
	private String respCode;
	
	private String respMsg;
	
	private String mode;
	
	private String sign;
	
	@Override
	public String getBizCode() {
		// TODO Auto-generated method stub
		return this.bizCode;
	}

	@Override
	public String getIdentityId() {
		// TODO Auto-generated method stub
		return this.identityId;
	}

	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}

	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getRespMsg() {
		return respMsg;
	}

	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

	public String getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

}
