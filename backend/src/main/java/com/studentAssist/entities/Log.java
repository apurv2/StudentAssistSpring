package com.studentAssist.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    int exceptionType;


    @ElementCollection
    private List<String> queryParameters = new ArrayList<String>();

    public Log(String logDetail, Date createDate, String userId, int exceptionType, List<String> queryParameters) {
        this.logDetail = logDetail;
        this.createDate = createDate;
        this.userId = userId;
        this.exceptionType = exceptionType;
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

    public int getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(int exceptionType) {
        this.exceptionType = exceptionType;
    }
}
