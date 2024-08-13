package com.employee.mapper;

import org.springframework.stereotype.Component;

import com.employee.dto.DepartmentDTO;
import com.employee.dto.EmployeeDTO;
import com.employee.model.Department;
import com.employee.model.Employee;

@Component
public class EmployeeMapper {

    public  EmployeeDTO toDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeId(employee.getEmployeeId());
        dto.setEmployeeName(employee.getEmployeeName());
        dto.setEmployeeEmail(employee.getEmployeeEmail());
        dto.setUsername(employee.getUsername());
        dto.setPassword(employee.getPassword());
        
        DepartmentDTO department = new DepartmentDTO();
        department.setDepartmentId(employee.getDepartment().getDepartmentId());
        department.setDepartmentName(employee.getDepartment().getDepartmentName());
        dto.setDepartment(department);
        return dto;
    }

    public  Employee toEntity(EmployeeDTO dto, Department department) {
        Employee employee = new Employee();
        employee.setEmployeeId(dto.getEmployeeId());
        employee.setEmployeeName(dto.getEmployeeName());
        employee.setEmployeeEmail(dto.getEmployeeEmail());
        employee.setUsername(dto.getUsername());
        employee.setPassword(dto.getPassword());
        employee.setDepartment(department);
        return employee;
    }
    
    public  DepartmentDTO toDTO(Department department) {
        if (department == null) {
            return null;
        }
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setDepartmentId(department.getDepartmentId());
        departmentDTO.setDepartmentName(department.getDepartmentName());
        return departmentDTO;
    }
}
