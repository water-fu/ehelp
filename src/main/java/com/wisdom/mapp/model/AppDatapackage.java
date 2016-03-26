package com.wisdom.mapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wisdom.mapp.core.model.IBody;
import com.wisdom.mapp.core.model.IHeader;
import com.wisdom.mapp.core.model.IMappDatapackage;

/**
 *
 * Created by fusj on 16/1/15.
 */
public class AppDatapackage implements IMappDatapackage {
	@JsonProperty
	private IHeader header;
	@JsonProperty
	private IBody body;

	public AppDatapackage() {
		super();
	}

	public AppDatapackage(AppHeader header) {
		super();
		this.header = header;
	}

	public AppDatapackage(AppHeader header, AppBody body) {
		super();
		this.header = header;
		this.body = body;
	}
	
	public IHeader getHeader() {
		return header;
	}

	public void setHeader(IHeader header) {
		this.header = header;
	}

	public IBody getBody() {
		return body;
	}

	public void setBody(IBody body) {
		this.body = body;
	}
}
