package com.revhire.model;

public class JobApplication {
    private int appId;
    private int jobId;
    private int userId;
    private String appstatus;
    private String resume;

    public JobApplication(int appId, int jobId, int userId, String resume, String appstatus) {
        this.appId = appId;
        this.jobId = jobId;
        this.userId = userId;
        this.resume=resume;
        this.appstatus = appstatus;
    }


    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getResume(){return resume;}

    public void setResume(){this.resume=resume;}

    public String getAppstatus() {
        return appstatus;
    }

    public void setAppstatus(String appstatus) {
        this.appstatus = appstatus;
    }
}
