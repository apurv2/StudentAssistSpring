package com.studentAssist.exceptionHandler;

public class ErrorInfo {

	public final String response;
	public final String message;

	/**
	 * 
	 * @param response
	 * @param code
	 */
	public ErrorInfo(String response, String code) {
		super();
		this.response = response;
		this.message = code;
	}

}
