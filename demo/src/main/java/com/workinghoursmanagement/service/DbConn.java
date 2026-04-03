package com.workinghoursmanagement.service;

import java.sql.*;

public class DbConn {
  private static final String URL = "jdbc:mysql://localhost:3306/gestionaleore";
  private static final String USER = "root";
  private static final String PASSWORD = "root";

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(URL, USER, PASSWORD);
  }
}