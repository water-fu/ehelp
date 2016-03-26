package com.wisdom.mapp.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.wisdom.mapp.core.model.IBody;
import org.apache.commons.beanutils.PropertyUtils;

import java.beans.PropertyDescriptor;

/**
 * 分装IBody接口，重写toString方法
 *
 * Constraint 详细信息
 * 
 * @Null 被注释的元素必须为 null
 * @NotNull 被注释的元素必须不为 null
 * @AssertTrue 被注释的元素必须为 true
 * @AssertFalse 被注释的元素必须为 false
 * @Min(value) 被注释的元素必须是一个数字，其值必须大于等于指定的最小值
 * @Max(value) 被注释的元素必须是一个数字，其值必须小于等于指定的最大值
 * @DecimalMin(value) 被注释的元素必须是一个数字，其值必须大于等于指定的最小值
 * @DecimalMax(value) 被注释的元素必须是一个数字，其值必须小于等于指定的最大值
 * @Size(max, min) 被注释的元素的大小必须在指定的范围内
 * @Digits (integer, fraction) 被注释的元素必须是一个数字，其值必须在可接受的范围内
 * @Past 被注释的元素必须是一个过去的日期
 * @Future 被注释的元素必须是一个将来的日期
 * @Pattern(value) 被注释的元素必须符合指定的正则表达式
 * @Email 被注释的元素必须是电子邮箱地址
 * @Length 被注释的字符串的大小必须在指定的范围内
 * @NotEmpty 被注释的字符串的必须非空
 * @Range 被注释的元素必须在合适的范围内
 *
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
public abstract class AppBody extends IBody {

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
