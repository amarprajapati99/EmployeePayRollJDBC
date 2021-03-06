package com.employee.payrollservices;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBService {
    private static EmployeePayrollDBService employeePayrollDBService;
    private PreparedStatement employeePayrollDataStatement;
    private PreparedStatement prepareStatement;
    private PreparedStatement updateEmployeeSalary;
    private PreparedStatement employeeSalary;

    public EmployeePayrollDBService() {
    }

    public static EmployeePayrollDBService getInstance() {
        if (employeePayrollDBService == null)
            employeePayrollDBService = new EmployeePayrollDBService();
        return employeePayrollDBService;
    }

    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/employee_payroll";
        String userName = "root";
        String password = "root";
        Connection connection;
        System.out.println("Connecting to database:" + jdbcURL);
        connection = DriverManager.getConnection(jdbcURL, userName, password);
        System.out.println("Connection is successful!!!!!!" + connection);
        return connection;
    }

    public List<EmployeePayrollData> readData() {
        String sql = "SELECT * FROM employee_payroll1; ";
        return this.getEmployeePayrollDataUsingDB(sql);
    }

    public int updateEmployeeData(String name, Double salary) {
        return this.updateEmployeeDataUsingStatement(name, salary);
    }

    private int updateEmployeeDataUsingStatement(String name, Double salary) {
        String sql = String.format("update employee_payroll1 set salary = %.2f where name = '%s';", salary, name);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
/*@Description -  to update the employee data using name and salary and we pass the exception
* if the name are not matching the invalid name we throw the exception and handling the catch block */
    public int updateEmployeeDataUsingPreparedStatement(String name, Double salary) throws EmployeePayrollException {
        String sql = "update employee_payroll1 set salary = ? where name = ?";
        if (this.updateEmployeeSalary == null)
            updateEmployeeSalary = this.prepareStatementForEmployeeData(sql);
        try {
            updateEmployeeSalary.setString(2, name);
            updateEmployeeSalary.setDouble(1, salary);
            return updateEmployeeSalary.executeUpdate();
        } catch (SQLException e) {
            throw new EmployeePayrollException("Wrong given Name", EmployeePayrollException.ExceptionType.WRONG_NAME);
        }
    }
/* @Description- inside we use sql query for the employee payroll data */

    public List<EmployeePayrollData> getEmployeePayrollData(String name) {
        List<EmployeePayrollData> employeePayrollList = null;
        String sql = "SELECT * FROM employee_payroll1 WHERE name = ?";
        if (this.employeePayrollDataStatement == null)
            employeePayrollDataStatement = this.prepareStatementForEmployeeData(sql);
        try {
            employeePayrollDataStatement.setString(1, name);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }
    /* @Description- inside we use sql query for the employee payroll data and get the data */

    public List<EmployeePayrollData> getSalary(String name, Double salary) {
        List<EmployeePayrollData> employeePayrollList = null;
        String sql = "SELECT * FROM employee_payroll1 WHERE name = ? AND salary = ?";
        if (this.employeeSalary == null)
            employeeSalary = this.prepareStatementForEmployeeData(sql);
        try {
            employeeSalary.setString(1, name);
            employeeSalary.setDouble(2, salary);
            ResultSet resultSet = employeeSalary.executeQuery();
            employeePayrollList = this.getEmployeePayrollData(resultSet);
            return employeePayrollList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /* @Description- inside we use sql query for the employee payroll data and get the data like id, name, salary
    * and date */

    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }
/* @Description -using prepared statement for get the employee data*/
    private PreparedStatement prepareStatementForEmployeeData(String sql) {
        try {
            Connection connection = this.getConnection();
            prepareStatement = connection.prepareStatement(sql);
            return prepareStatement;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /* @Description -using prepared statement for get the employee data to given date range*/
    public List<EmployeePayrollData> getEmployeePayrollDataForDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = String.format("SELECT * FROM employee_payroll WHERE START BETWEEN '%s' AND '%s';",
                Date.valueOf(startDate), Date.valueOf(endDate));
        return this.getEmployeePayrollDataUsingDB(sql);
    }

    private List<EmployeePayrollData> getEmployeePayrollDataUsingDB(String sql) {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }
}