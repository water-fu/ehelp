package com.wisdom.mapp.core.model;

import java.util.List;

public class M9999Request extends IBody {

	private String method;
	
	private String interfaceClass;
	
	private String interfaceKey;
	
	private List<?> params;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getInterfaceClass() {
		return interfaceClass;
	}

	public void setInterfaceClass(String interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	public List<?> getParams() {
		return params;
	}

	public void setParams(List<?> params) {
		this.params = params;
	}

	public String getInterfaceKey() {
		return interfaceKey;
	}

	public void setInterfaceKey(String interfaceKey) {
		this.interfaceKey = interfaceKey;
	}
	
}
