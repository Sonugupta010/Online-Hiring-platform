package unitTest;

//package com.revhire.dao;

import com.revhire.dao.UserImpl;
import com.revhire.model.JobSeeker;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserImplTest {

    private static Connection connection;
    private static UserImpl userImpl;

    @BeforeAll
    public static void setUp() throws SQLException {
        connection = mock(Connection.class);
        userImpl = UserImpl.getJobSeekerInstance(connection);
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testJobSeekerRegister_Success() throws SQLException {
        // Arrange
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Act
        JobSeeker jobSeeker = new JobSeeker("testUser", "01/01/2000", "test@example.com", "password","resume");
        userImpl.JobSeekerRegister(jobSeeker);

        // Assert
        // Verify that the executeUpdate method is called
        Mockito.verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testJobSeekerRegister_Failure() throws SQLException {
        // Arrange
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        // Act
        JobSeeker jobSeeker = new JobSeeker("testUser", "01/01/2000", "test@example.com","password", "resume");
        userImpl.JobSeekerRegister(jobSeeker);

        // Assert
        // Verify that the executeUpdate method is called
        Mockito.verify(preparedStatement).executeUpdate();
    }

    // Similarly, write test methods for other methods in the UserImpl class
    // such as validateJobSeekerLogin(), getJobs(), myApplication(), applyForJob(), etc.
}

