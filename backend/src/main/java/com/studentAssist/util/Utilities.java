package com.studentAssist.util;

import java.util.Date;
import java.util.TimeZone;

public class Utilities {

public static Date getDate()
{
	TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

	Date date = new Date();
	date.setHours(date.getHours() - 4);
	System.out.println(date);

	return date;	

}

}
