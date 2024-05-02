package com.revhire.model;

public class Employer {

    private String Ename;
    private String Department;
    private String Eemail;
    private String Position;
    private  String CompanyName;
    private  String Password;

    public Employer(){

    }
    public Employer(String ename, String department, String eemail, String position, String companyName,String password) {
        Ename = ename;
        Department = department;
        Eemail = eemail;
        Position = position;
        CompanyName = companyName;
        Password=password;

    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEname() {
        return Ename;
    }

    public void setEname(String ename) {
        Ename = ename;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getEemail() {
        return Eemail;
    }

    public void setEemail(String eemail) {
        Eemail = eemail;
    }

    public String getPosition(){
        return Position;
    }

    public void setPosition(String position){Position=position;}

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }
}

