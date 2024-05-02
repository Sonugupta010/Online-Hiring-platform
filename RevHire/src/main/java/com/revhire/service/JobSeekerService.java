package com.revhire.service;

import com.revhire.dao.User;
import com.revhire.model.JobSeeker;

public class JobSeekerService {

    private User jobSeeker;

    public JobSeekerService(User userDAO) {
        this.jobSeeker = userDAO;
    }

    public void JobSeekerRegister(JobSeeker jobSeekerModel) {
        jobSeeker.JobSeekerRegister(jobSeekerModel);
    }

    public void validateJobSeekerLogin(){
        jobSeeker.validateJobSeekerLogin();
    }
}
