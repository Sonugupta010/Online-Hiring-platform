package com.revhire.model;

public class JobSeeker {

    private String UserName;
    private String dob;
    private  String Email;
    private String Password;
    private String resume;

    public JobSeeker(String userName, String dob, String email, String password, String resume) {
        UserName = userName;
        this.dob = dob;
        Email = email;
        Password = password;
        this.resume=resume;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getResume(){return resume;}

    public void setResume(){this.resume=resume;}
}
