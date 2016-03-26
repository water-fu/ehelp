package com.wisdom.mapp.core.exception;


public class MappException extends BusinessException {

	public  MappException(MappExceptionCode exCode, Throwable t) {
		super(exCode, t);
	}

	public  MappException(MappExceptionCode exCode) {
		super(exCode);
	}
}
