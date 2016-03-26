package com.wisdom.mapp.core.filter;


/**
 *
 */
public interface IFilter<T> 
{
	public void init() throws Exception;
	
	public void destroy() throws Exception;
	
	public T doFilter(T context) throws Exception;
}
