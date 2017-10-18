package com.studentAssist.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Log")
public class Log {
	@Id
	@GeneratedValue
	int logId;
	@Column(name = "logDetail", length = 5000)
	String logDetail;

	Date createDate;

	String userId;

	@ElementCollection
	private List<String> queryParameters = new ArrayList<String>();

	public Log() {
	}

	public Log(String logDetail, Date createDate, String userId, List<String> queryParameters) {
		super();
		this.logDetail = logDetail;
		this.createDate = createDate;
		this.userId = userId;
		this.queryParameters = queryParameters;
	}

	public List<String> getQueryParameters() {
		return queryParameters;
	}

	public void setQueryParameters(List<String> queryParameters) {
		this.queryParameters = queryParameters;
	}

	public int getLogId() {
		return logId;
	}

	public void setLogId(int logId) {
		this.logId = logId;
	}

	public String getLogDetail() {
		return logDetail;
	}

	public void setLogDetail(String logDetail) {
		this.logDetail = logDetail;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
