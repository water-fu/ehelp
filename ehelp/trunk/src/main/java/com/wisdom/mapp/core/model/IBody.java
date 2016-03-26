package com.wisdom.mapp.core.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.commons.beanutils.PropertyUtils;

import java.beans.PropertyDescriptor;

/**
 *
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
public abstract class IBody {

	@Override
	public String toString() 
	{
		String ret = "";
		
		try{
			
			StringBuffer string = new StringBuffer("");
			
			PropertyDescriptor[] props = PropertyUtils.getPropertyDescriptors(this.getClass());
			
			if(props == null || props.length==0)
			{
				return this.getClass().getName() + "[ ]";
			}
			
			for(PropertyDescriptor prop : props)
			{
				if(prop.getPropertyType() == Class.class)
					continue;

				string.append(prop.getName() + "=" + PropertyUtils.getProperty(this, prop.getName()) + ", ");
			}
			
			ret = string.toString().substring(0, string.toString().lastIndexOf(","));
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return this.getClass().getName() + "[ "+ret+" ] ";
	}
	
}
