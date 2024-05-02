package com.revhire.service;

import com.revhire.dao.User;
import com.revhire.model.Employer;

public class EmployerService {

    private User employer;

    public EmployerService(User userDAO) {
        this.employer = userDAO;
    }

    public void validateEmployerLogin(){
        employer.validateEmployerLogin();
    }
    public  void EmployerRegister(Employer empl){
        employer.EmployerRegister(empl);
    }
}

