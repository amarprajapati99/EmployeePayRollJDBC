package com.employee.payrollservices;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class EmployeePayrollServicesTest {
    EmployeePayrollService employeePayrollService;
    List<EmployeePayrollData> employeePayrollList;

    @Test
    public void givenEmployeePayrollInDB_WhenRetrived_ShouldMatchEmployeeCount() {
        employeePayrollService = new EmployeePayrollService();
        employeePayrollList = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Assertions.assertEquals(3, employeePayrollList.size());
    }

}
