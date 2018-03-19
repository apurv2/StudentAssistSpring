package com.studentAssist.exception;

public class BadStudentRequestException extends Exception {

	String msg;
	int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String toString(String msg, int code) {
		return msg + code;

	}

}
