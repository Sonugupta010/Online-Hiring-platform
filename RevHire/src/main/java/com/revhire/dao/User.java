package com.revhire.dao;

import java.util.*;

import com.revhire.model.Employer;
import com.revhire.model.JobSeeker;
import com.revhire.model.JobOpening;

public interface User {
    void JobSeekerRegister(JobSeeker jobSeeker) ;
    void JobSeekerLogin();
    void getJobs();
    void applyForJob();
    void myApplication();
    void EmployerRegister(Employer employerModel);
    void EmployerLogin();
    void postJob();
    void deleteJob();
    void viewApplications();
    void validateJobSeekerLogin();
    void validateEmployerLogin();
}
