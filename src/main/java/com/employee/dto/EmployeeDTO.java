package com.employee.dto;


import lombok.Data;

@Data
public class EmployeeDTO {
    private Long employeeId;
    private String employeeName;
    private String employeeEmail;
    private String username;
    private String password;
    private DepartmentDTO department; // Reference to DepartmentDTO
}
