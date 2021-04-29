package com.employee.payrollservices;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBService {
    private PreparedStatement employeePayrollDataStatement;
    private static EmployeePayrollDBService employeePayrollDBService;
    private PreparedStatement updateEmployeeSalary;

    public EmployeePayrollDBService() {}

    public static EmployeePayrollDBService getInstance() {
        if (employeePayrollDBService == null)
            employeePayrollDBService = new EmployeePayrollDBService();
        return employeePayrollDBService;
    }
/* @Description- To connect the local host server jdbc */

    private Connection getConnection() throws SQLException, SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/employee_payroll";
        String userName = "root";
        String userPass = "root";
        Connection connection;
        System.out.println("Connecting to database:"+jdbcURL);
        connection = DriverManager.getConnection(jdbcURL,userName,userPass);
        System.out.println("Connection is successful!!!!!! "+ connection);
        return connection;
    }

    /* @Description- we used the sql query for list of the all employee payroll  */

    public List<EmployeePayrollData> readData() {
        String sql= "SELECT * FROM employee_payroll1; ";
        List<EmployeePayrollData> employeePayrollDataList = new ArrayList<>();
        try {
            Connection connection = this.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                double salary = result.getDouble("salary");
                LocalDate startDate = result.getDate("start").toLocalDate();
                employeePayrollDataList.add(new EmployeePayrollData(id, name, salary, startDate));
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollDataList;
    }

    int updateEmployeeData(String name, Double salary) {
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
/* @Description- to update the employee data like name and salary .*/

    public int updateEmployeeDataUsingPreparedStatement(String name, Double salary){
        List<EmployeePayrollData> employeePayrollList = null;
        if (this.updateEmployeeSalary == null)
            this.prepareStatementForToUpdateSalary();
        try {
            updateEmployeeSalary.setString(2, name);
            updateEmployeeSalary.setDouble(1, salary);
            return updateEmployeeSalary.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<EmployeePayrollData> getEmployeePayrollData(String name) {
        List<EmployeePayrollData> employeePayrollList = null;
        if (this.employeePayrollDataStatement == null)
            this.prepareStatementForEmployeeData();
        try {
            employeePayrollDataStatement.setString(1, name);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

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
    /* Description- To update the employee data  using prepared statement */

    private void prepareStatementForEmployeeData() {
        try {
            Connection connection = this.getConnection();
            String sql = "SELECT * FROM employee_payroll1 WHERE name = ?";
            employeePayrollDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
/* Description- To update the salary using prepared statement */

    private void prepareStatementForToUpdateSalary() {
        try {
            Connection connection = this.getConnection();
            String sql = "update employee_payroll set salary = ? where name = ?";
            updateEmployeeSalary = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
