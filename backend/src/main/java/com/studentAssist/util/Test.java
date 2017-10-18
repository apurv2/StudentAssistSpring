package com.studentAssist.util;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Test {

	String fname;
	String lname;
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public Test(String fname, String lname) {
		this.fname = fname;
		this.lname = lname;
	}
	@Override
	public String toString() {
		return "Test [fname=" + fname + ", lname=" + lname + "]";
	}
	
	Test()
	{}
	
}
