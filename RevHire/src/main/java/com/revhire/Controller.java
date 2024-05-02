package com.revhire;

import com.revhire.dao.User;
import com.revhire.dao.UserImpl;
import com.revhire.model.Employer;
import com.revhire.model.JobSeeker;
import com.revhire.service.EmployerService;
import com.revhire.service.JobSeekerService;
import com.revhire.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    public static void controller(String[] args) {

        try (Connection connection = DBUtil.getConnection()) {
            System.out.println("Connected to the database.");

            User userDAO = UserImpl.getJobSeekerInstance(connection);
            EmployerService employerService = new EmployerService(userDAO);
            JobSeekerService jobSeekerService = new JobSeekerService(userDAO);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\nWho You Are:");
                System.out.println("1.Job Seeker");
                System.out.println("2.Employer");
                System.out.println("3.Exit");

                int choice;
                try {
                    choice = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid Input Format");
                    scanner.nextLine(); // Clear the invalid input
                    continue; // Restart the loop to ask for input again
                }

                switch (choice) {
                    case 1:
                        System.out.println("Enter Your Choice To Proceed");
                        System.out.println("1.Login");
                        System.out.println("2.Register");
                        int innerJsChoice;
                        try {
                            innerJsChoice = scanner.nextInt();
                        } catch (InputMismatchException ex) {
                            System.out.println("Invalid Input Format");
                            scanner.nextLine(); // Clear the invalid input
                            break; // Continue to the outer loop
                        }

                        switch (innerJsChoice) {
                            case 1:
                                jobSeekerService.validateJobSeekerLogin();
                                break;
                            case 2:
                                // Job Seeker Registration
                                System.out.println("Enter Your User Name:");
                                String userName = scanner.next();
                                String dob;
                                String invalidDobMessage;
                                do {
                                    System.out.println("Enter Your Date Of Birth (dd/mm/yyyy):");
                                    dob = scanner.next();
                                    invalidDobMessage = isValidDOBMessage(dob);
                                    if (!invalidDobMessage.isEmpty()) {
                                        System.out.println(invalidDobMessage);
                                    }
                                } while (!invalidDobMessage.isEmpty());

                                System.out.println("Enter Your User Email:");
                                String email = scanner.next();
                                while (!isValidEmail(email)) {
                                    System.out.println("Invalid email format. Enter a valid email:");
                                    email = scanner.next();
                                }

                                System.out.println("Enter Your User Password:");
                                String password = scanner.next();

                                System.out.println("Provide your Resume link: ");
                                String resume = scanner.next();

                                // Create Model JobSeeker Object
                                JobSeeker jobSeeker = new JobSeeker(userName, dob, email, password,resume);
                                jobSeekerService.JobSeekerRegister(jobSeeker);
                                break;
                            default:
                                System.out.println("Enter Valid Choice");
                        }
                        break;
                    case 2:
                        System.out.println("Enter Your Choice To Proceed");
                        System.out.println("1.Login");
                        System.out.println("2.Register");
                        int innerEmpChoice;
                        try {
                            innerEmpChoice = scanner.nextInt();
                        } catch (InputMismatchException ex) {
                            System.out.println("Invalid Input Format");
                            scanner.nextLine(); // Clear the invalid input
                            break; // Continue to the outer loop
                        }

                        switch (innerEmpChoice) {
                            case 1:
                                employerService.validateEmployerLogin();
                                break;
                            case 2:
                                // Employer Registration
                                System.out.println("Enter Your User Name:");
                                String user_name=scanner.next();
                                System.out.println("Enter Your Department");
                                String dob=scanner.next();
                                System.out.println("Enter Your User Email:");
                                String email = scanner.next();
                                while (!isValidEmail(email)) {
                                    System.out.println("Invalid email format. Enter a valid email:");
                                    email = scanner.next();
                                }

                                System.out.println("Enter Your User Position:");
                                String position= scanner.next();
                                System.out.println("Enter Your Company Name:");
                                String company_name=scanner.next();
                                System.out.println("Enter Your User Password:");
                                String password=scanner.next();

                                Employer employer=new Employer(user_name,dob,email,position,company_name,password);
                                employerService.EmployerRegister(employer);
                                break;
                            default:
                                System.out.println("Enter Valid Choice");
                        }
                        break;
                    case 3:
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            logger.error("Connection failed. Error: " + e.getMessage());
            System.out.println("Connection failed. Error: " + e.getMessage());
        }
    }

    private static String isValidDOBMessage(String dob) {
        // Regex for date validation (dd/mm/yyyy)
        String dobRegex = "^\\d{2}/\\d{2}/\\d{4}$";
        Pattern pattern = Pattern.compile(dobRegex);
        Matcher matcher = pattern.matcher(dob);
        if (matcher.matches()) {
            return ""; // Valid date format
        } else {
            return "Invalid date format. Please enter the date in the format dd/mm/yyyy:";
        }
    }

    private static boolean isValidEmail(String email) {
        // Regex for email validation
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
