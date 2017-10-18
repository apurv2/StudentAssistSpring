package com.studentAssist.exception;

public class StudenAssitException extends Exception {
	String msg;
	int code;

	public StudenAssitException(String msg)
	{
		this.msg=msg;
	}
	
	public StudenAssitException(String msg,int code)
	{
		this.msg=msg;
		this.code=code;
	}
	public int getCode() {
		return code;
	}

	public void setCode(int  code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String toString(String msg,int code)
	{
		return msg+code;
		
	}
}
