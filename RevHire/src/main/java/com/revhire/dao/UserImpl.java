package com.revhire.dao;

import com.revhire.model.JobSeeker;
import com.revhire.Main;
import com.revhire.model.Employer;
import com.revhire.model.JobApplication;
import com.revhire.model.JobOpening;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserImpl implements User {

    private static final Logger logger = LoggerFactory.getLogger(UserImpl.class);
    private static UserImpl instance;
    private final Connection connection;
    Scanner sc=new Scanner(System.in);
    private  int userid;
    private  int employerid;

    // Private constructor to prevent external instantiation
    private UserImpl(Connection connection) {
        this.connection = connection;
    }

    public static  UserImpl getJobSeekerInstance(Connection connection) {
        if (instance == null) {
            instance = new UserImpl(connection);
        }
        return instance;
    }

    @Override
    public void JobSeekerRegister(JobSeeker jobSeeker) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO JOBSEEKER (UserName, dob,email,password, resume) VALUES (?, ?, ?, ?, ?)");

            preparedStatement.setString(1, jobSeeker.getUserName());
            preparedStatement.setString(2, jobSeeker.getDob());
            preparedStatement.setString(3, jobSeeker.getEmail());
            preparedStatement.setString(4, jobSeeker.getPassword());
            preparedStatement.setString(5, jobSeeker.getResume());
            int res=preparedStatement.executeUpdate();
            if(res>0){
                System.out.println("User Registered Successfully");
                logger.info("Job Seeker Registered");
                validateJobSeekerLogin();
            }
            else{
                System.out.println("Registration Failed Try Again");
                logger.warn("Registration Failed");
                JobSeekerRegister(jobSeeker);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void validateJobSeekerLogin() {
        System.out.println("Please Login");
        System.out.println("Enter Your UserName");
        String userName=sc.next();
        System.out.println("Enter Your Password");
        String password=sc.next();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM JOBSEEKER WHERE UserName=? AND Password=?");
            preparedStatement.setString(1,userName);
            preparedStatement.setString(2,password);
            ResultSet res=preparedStatement.executeQuery();
            if(res.next()){
                this.userid=res.getInt("userID");
                logger.info("User Logged In");
                JobSeekerLogin();
            }
            else{
                System.out.println("Please Enter Valid Details");
                logger.warn("Wrong Credentials");
                validateJobSeekerLogin();
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }


    @Override
    public void JobSeekerLogin() {
        int choice = 0;
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to RevHire");
            System.out.println("1. Get Job Openings");
            System.out.println("2. Get My Applications");
            System.out.println("3. Exit");

            try {
                choice = sc.nextInt();
                if (choice < 1 || choice > 3) {
                    System.out.println("Error: Please enter a valid choice (1-3)");
                    sc.nextLine(); // Clear the input buffer
                    continue; // Ask for input again
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter a number");
                sc.nextLine(); // Clear the input buffer
                continue; // Ask for input again
            }

            // Perform action based on the validated choice
            switch (choice) {
                case 1:
                    getJobs();
                    break;
                case 2:
                    myApplication();
                    break;
                case 3:
                    System.exit(0);
                    break;
            }
        }
    }

    @Override
    public void getJobs() {
        List<JobOpening> jobOpenings = new ArrayList<>();
        System.out.println("Enter Details To Filter");
        System.out.println("Enter Company Name");
        String companyName=sc.next();
        System.out.println("Enter Location");
        String location=sc.next();
        System.out.println("Enter Experience");
        int experience=sc.nextInt();
        System.out.println("Enter Job Role");
        String jobRole=sc.next();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM JobOpenings Where CompanyName=? AND jobLocation=? AND YearsOfExperience=? AND JobRole=? ");
            statement.setString(1,companyName);
            statement.setString(2,location);
            statement.setInt(3,experience);
            statement.setString(4,jobRole);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                jobOpenings.add(new JobOpening(
                        resultSet.getInt("Jobid"),
                        resultSet.getString("CompanyName"),
                        resultSet.getString("JobRole"),
                        resultSet.getString("jobLocation"),
                        resultSet.getInt("Package"),
                        resultSet.getString("jobDescription"),
                        resultSet.getInt("noOfPositions"),
                        resultSet.getInt("YearsOfExperience"),
                        resultSet.getInt("EmployeeID")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (JobOpening emp : jobOpenings) {
            System.out.println("ID:  " + emp.getJobId()+"   Company Name:   "+emp.getCompanyName()+"   Job Role:   "+emp.getJobRole()+"   Job Location:   "+emp.getJobLocation()+"    Package:   "+emp.getPackageAmount()+"  Job Description:  "+emp.getJobDescription()+"   No.Of Positions:  "+emp.getNoOfPositions()+"   Years Of Experience:   "+emp.getYearsOfExperience()+"   Posted By:   "+emp.getEmployeeId());
        }
        System.out.println("1.Apply For The Job");
        System.out.println("2.Go To Main Menu");
        int choice=sc.nextInt();
        switch (choice){
            case 1:
                applyForJob();
                break;
            case 2:
                JobSeekerLogin();
                break;
            default:
                System.out.println("Please Enter Valid Choice");
                JobSeekerLogin();
        }
    }


//    @Override
//    public void myApplication() {
//
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Jobapplication Where userid=? ");
//            preparedStatement.setInt(1,userid);
//            ResultSet result=preparedStatement.executeQuery();
//            List <JobApplication> jobApplications=new ArrayList<>();
//            while (result.next()){
//                jobApplications.add(new JobApplication(
//                        result.getInt("Appid"),
//                        result.getInt("jobid"),
//                        result.getInt("userid"),
//                        result.getString("resume"),
//                        result.getString("appstatus")
//                ));
//            }
//            System.out.println("Your Applications");
//            for (JobApplication job:jobApplications){
//                System.out.println("Application: " + job.getAppId()+" Job ID: "+job.getJobId()+" Resume: "+job.getResume()+" Application Status: "+job.getAppstatus());
//            }
//            System.out.println("1.Withdraw Application");
//            System.out.println("2.Main Menu");
//            int choice=sc.nextInt();
//            switch (choice){
//                case 1:
//                    System.out.println("Enter Application ID To Withdraw");
//                    int appId=sc.nextInt();
//                    preparedStatement=connection.prepareStatement("DELETE FROM JobApplication Where appId=?;");
//                    preparedStatement.setInt(1,appId);
//                    int rows=preparedStatement.executeUpdate();
//                    if(rows>0){
//                        System.out.println("Successfully Unregistered");
//                        logger.info("Withdrawn His Applications");
//                        JobSeekerLogin();
//                    }
//                    else{
//                        System.out.println("Withdraw Failed");
//                        JobSeekerLogin();
//                    }
//                    break;
//                case 2:
//                    JobSeekerLogin();
//                    break;
//                default:
//                    System.out.println("Enter Valid Choice");
//                    myApplication();
//                    break;
//            }
//        }
//        catch (SQLException e){
//            e.printStackTrace();
//        }
//
//    }

    @Override
    public void myApplication() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT ja.Appid, ja.jobid, ja.userid, js.resume, ja.appstatus FROM Jobapplication ja INNER JOIN JOBSEEKER js ON ja.userid = js.userid WHERE ja.userid=?");
            preparedStatement.setInt(1, userid);
            ResultSet result = preparedStatement.executeQuery();
            List<JobApplication> jobApplications = new ArrayList<>();
            while (result.next()) {
                jobApplications.add(new JobApplication(
                        result.getInt("Appid"),
                        result.getInt("jobid"),
                        result.getInt("userid"),
                        result.getString("resume"),
                        result.getString("appstatus")
                ));
            }
            System.out.println("Your Applications:");
            for (JobApplication job : jobApplications) {
                System.out.println("Application ID: " + job.getAppId() + ", Job ID: " + job.getJobId() + ", Resume: " + job.getResume() + ", Application Status: " + job.getAppstatus());
            }
            System.out.println("1.Withdraw Application");
            System.out.println("2.Main Menu");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Enter Application ID To Withdraw");
                    int appId = sc.nextInt();
                    preparedStatement = connection.prepareStatement("DELETE FROM JobApplication WHERE appId=? AND userid=?");
                    preparedStatement.setInt(1, appId);
                    preparedStatement.setInt(2, userid);
                    int rows = preparedStatement.executeUpdate();
                    if (rows > 0) {
                        System.out.println("Successfully Withdrawn");
                        logger.info("Withdrawn His Applications");
                        JobSeekerLogin();
                    } else {
                        System.out.println("Withdrawal Failed");
                        JobSeekerLogin();
                    }
                    break;
                case 2:
                    JobSeekerLogin();
                    break;
                default:
                    System.out.println("Enter Valid Choice");
                    myApplication();
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void applyForJob(){
        List<JobOpening> availableJobs = getAllAvailableJobs(); // Fetch list of all available jobs
        if (availableJobs.isEmpty()) {
            System.out.println("No jobs available to apply.");
            JobSeekerLogin();
            return;
        }

        // Display available jobs
        System.out.println("Available Jobs:");
        for (JobOpening job : availableJobs) {
            System.out.println("ID: " + job.getJobId() + ", Company: " + job.getCompanyName() + ", Role: " + job.getJobRole());
        }

        System.out.println("Enter Job ID to apply:");
        int selectedJobId = sc.nextInt();

        // Check if the selected job ID is valid
        boolean isValidJobId = false;
        for (JobOpening job : availableJobs) {
            if (job.getJobId() == selectedJobId) {
                isValidJobId = true;
                break;
            }
        }

        if (!isValidJobId) {
            System.out.println("Invalid Job ID. Please enter a valid Job ID.");
            applyForJob(); // Restart the method to prompt user for valid input
            return;
        }

        try {
            PreparedStatement findEmployeeId = connection.prepareStatement("SELECT EmployeeID FROM Jobopenings WHERE Jobid=?");
            findEmployeeId.setInt(1, selectedJobId);
            ResultSet empResult = findEmployeeId.executeQuery();
            if (empResult.next()) {
                int employee_id = empResult.getInt("EmployeeID");
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Jobapplication (EmployeeID, UserID, JobID, AppStatus) VALUES (?, ?, ?, ?)");
                    preparedStatement.setInt(1, employee_id);
                    preparedStatement.setInt(2, userid);
                    preparedStatement.setInt(3, selectedJobId);
                    preparedStatement.setString(4, "Pending");
                    int rows = preparedStatement.executeUpdate();
                    if (rows > 0) {
                        System.out.println("Applied Successfully");
                        logger.info("Applied For The Job");
                        JobSeekerLogin();
                    } else {
                        System.out.println("Application Failed");
                        JobSeekerLogin();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to fetch list of all available jobs
    private List<JobOpening> getAllAvailableJobs() {
        List<JobOpening> jobOpenings = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM JobOpenings");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                jobOpenings.add(new JobOpening(
                        resultSet.getInt("Jobid"),
                        resultSet.getString("CompanyName"),
                        resultSet.getString("JobRole"),
                        resultSet.getString("jobLocation"),
                        resultSet.getInt("Package"),
                        resultSet.getString("jobDescription"),
                        resultSet.getInt("noOfPositions"),
                        resultSet.getInt("YearsOfExperience"),
                        resultSet.getInt("EmployeeID")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobOpenings;
    }


    @Override
    public void EmployerRegister(Employer employerModel) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Employee (EName, Department,Eemail,Position,CompanyName,password) VALUES (?, ?, ?, ?,?,?)");

            preparedStatement.setString(1,employerModel.getEname());
            preparedStatement.setString(2, employerModel.getDepartment());
            preparedStatement.setString(3, employerModel.getEemail());
            preparedStatement.setString(4, employerModel.getPosition());
            preparedStatement.setString(5, employerModel.getCompanyName());
            preparedStatement.setString(6, employerModel.getPassword());
            int res=preparedStatement.executeUpdate();
            if(res>0){
                System.out.println("User Registered Successfully");
                validateEmployerLogin();
            }
            else{
                System.out.println("Registration Failed");
                EmployerRegister(employerModel);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }


    @Override
    public void validateEmployerLogin() {
        System.out.println("Please Login");
        System.out.println("Enter Your UserName");
        String userName=sc.next();
        System.out.println("Enter Your Password");
        String password=sc.next();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Employee WHERE EName=? AND password=?");
            preparedStatement.setString(1,userName);
            preparedStatement.setString(2,password);
            ResultSet res=preparedStatement.executeQuery();
            if(res.next()){
                this.employerid=res.getInt("EmployeeID");
                EmployerLogin();
            }
            else{
                System.out.println("Please Enter Valid Details");
                validateEmployerLogin();
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }

    public  void EmployerLogin(){
        System.out.println("Welcome");
        System.out.println("Enter Your Choice");
        System.out.println("1.Post Job");
        System.out.println("2.View Applications");
        System.out.println("3.Delete Job");
        System.out.println("4.Exit");
        Scanner sc = new Scanner(System.in);
        int choice= sc.nextInt();

        switch (choice){
            case 1:
                postJob();
                break;
            case 2:
                viewApplications();
                break;
            case 4:
                System.exit(0);
                break;
            case 3:
                deleteJob();
                break;
            default:
                System.out.println("Enter Valid Choice");
                break;
        }

    }


    @Override
    public void viewApplications() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT JA.*, JS.resume FROM Jobapplication JA INNER JOIN JobSeeker JS ON JA.UserID = JS.UserID WHERE JA.EmployeeID=?");
            preparedStatement.setInt(1, employerid);
            ResultSet result = preparedStatement.executeQuery();
            List<JobApplication> jobApplications = new ArrayList<>();
            while (result.next()) {
                jobApplications.add(new JobApplication(
                        result.getInt("Appid"),
                        result.getInt("jobid"),
                        result.getInt("userid"),
                        result.getString("resume"), // Add resume link to JobApplication
                        result.getString("appstatus")

                ));
            }
            System.out.println("Applications");
            for (JobApplication job : jobApplications) {
                System.out.println("Application ID: " + job.getAppId() + ", Job ID: " + job.getJobId() + ", Resume: "+job.getResume()+ ", Application Status: " + job.getAppstatus());
            }
            System.out.println("1.Reject");
            System.out.println("2.ShortList");
            System.out.println("3.Main Menu");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Enter Application ID To Reject");
                    int appId = sc.nextInt();
                    preparedStatement = connection.prepareStatement("UPDATE JobApplication SET appStatus='Rejected' WHERE Appid=?");
                    preparedStatement.setInt(1, appId);
                    int rows = preparedStatement.executeUpdate();
                    if (rows > 0) {
                        System.out.println("Successfully Updated");
                        viewApplications();
                    } else {
                        System.out.println("Failed");
                        viewApplications();
                    }
                    break;
                case 2:
                    System.out.println("Enter Application ID To Shortlist");
                    int applicationId = sc.nextInt();
                    preparedStatement = connection.prepareStatement("UPDATE JobApplication SET appStatus='Shortlisted' WHERE Appid=?");
                    preparedStatement.setInt(1, applicationId);
                    int resultRows = preparedStatement.executeUpdate();
                    if (resultRows > 0) {
                        System.out.println("Successfully Updated");
                        viewApplications();
                    } else {
                        System.out.println("Failed");
                        viewApplications();
                    }
                    break;
                case 3:
                    EmployerLogin();
                    break;
                default:
                    System.out.println("Enter Valid Choice");
                    viewApplications();
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void postJob() {
        try{
            System.out.println("Enter Job ID:");
            int jobId = sc.nextInt();

            System.out.println("Enter Company Name:");
            String companyName = sc.next();

            System.out.println("Enter Job Role:");
            String jobRole = sc.next();

            System.out.println("Enter Job Location:");
            String jobLocation = sc.next();

            System.out.println("Enter Package Amount:");
            int packageAmount = sc.nextInt();

            System.out.println("Enter Job Description:");
            String jobDescription = sc.next();

            System.out.println("Enter Number of Positions:");
            int noOfPositions = sc.nextInt();

            System.out.println("Enter Years of Experience:");
            int yearsOfExperience = sc.nextInt();

            JobOpening jobOpeningModel=new JobOpening(jobId,companyName,jobRole,jobLocation,packageAmount,jobDescription,noOfPositions,yearsOfExperience,employerid);
            // Prepare and execute the INSERT statement
            PreparedStatement statement = connection.prepareStatement("INSERT INTO JobOpenings (Jobid, CompanyName, JobRole, jobLocation, Package, jobDescription, NoOfPositions, YearsOfExperience, EmployeeID) VALUES (?, ?, ?, ?, CAST(? AS MONEY), ?, ?, ?, ?)");

            // Set values for each parameter in the INSERT statement
            statement.setInt(1, jobOpeningModel.getJobId());
            statement.setString(2, jobOpeningModel.getCompanyName());
            statement.setString(3, jobOpeningModel.getJobRole());
            statement.setString(4, jobOpeningModel.getJobLocation());
            statement.setInt(5, jobOpeningModel.getPackageAmount());
            statement.setString(6, jobOpeningModel.getJobDescription());
            statement.setInt(7, jobOpeningModel.getNoOfPositions());
            statement.setInt(8, jobOpeningModel.getYearsOfExperience());
            statement.setInt(9, jobOpeningModel.getEmployeeId());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Job posted successfully.");
                EmployerLogin();
            } else {
                System.out.println("Failed to post job Try Again");
                postJob();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteJob() {
        System.out.println("Your Job Postings\n");
        List<JobOpening> jobOpenings = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM JobOpenings where EmployeeID=?");
            statement.setInt(1,employerid);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                jobOpenings.add(new JobOpening(
                        resultSet.getInt("Jobid"),
                        resultSet.getString("CompanyName"),
                        resultSet.getString("JobRole"),
                        resultSet.getString("jobLocation"),
                        resultSet.getInt("Package"),
                        resultSet.getString("jobDescription"),
                        resultSet.getInt("noOfPositions"),
                        resultSet.getInt("YearsOfExperience"),
                        resultSet.getInt("EmployeeID")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (JobOpening emp : jobOpenings) {
            System.out.println("ID:  " + emp.getJobId()+"  Company Name:  "+emp.getCompanyName()+"  Job Role:  "+emp.getJobRole()+"  Job Location:  "+emp.getJobLocation()+"  Package:   "+emp.getPackageAmount()+"  Job Description:   "+emp.getJobDescription()+"   No.Of Positions:   "+emp.getNoOfPositions()+"   Years Of Experience:   "+emp.getYearsOfExperience()+"   Posted By:   "+emp.getEmployeeId());
        }
        System.out.println("Enter Job ID To Delete");
        int jobId=sc.nextInt();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("Delete from jobOpenings where Jobid=?");
            preparedStatement.setInt(1,jobId);
            int rows=preparedStatement.executeUpdate();
            if(rows>0){
                System.out.println("Deleted Successfully");
                deleteJob();
            }
            else{
                System.out.println("Failed To Delete");
                deleteJob();
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}
