package net.najiboulhouch.redditcloneapp;

import org.testcontainers.containers.MySQLContainer;

public class BaseTest {

    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.0.26")
            .withDatabaseName("spring-reddit-test-db")
            .withUsername("testuser")
            .withPassword("pass");

    static {
        mySQLContainer.start();
    }
}
