package com.wisdom.mapp.core.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public abstract class AbstractSettings {

	private static ObjectMapper mapper = new ObjectMapper();


	@Override
	public String toString() {
		try {
			return mapper.writeValueAsString(this);
		} catch ( Exception e ) {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
		}
	}
}
