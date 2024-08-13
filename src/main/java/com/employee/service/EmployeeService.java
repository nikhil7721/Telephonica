package com.employee.service;

import java.util.List;
import java.util.Optional;

import com.employee.dto.DepartmentDTO;
import com.employee.dto.EmployeeDTO;
import com.employee.model.Department;
import com.employee.model.Employee;

public interface EmployeeService {
	
	public EmployeeDTO addEmployee(Employee employee);
	
	public Optional<List<EmployeeDTO>>getAllEmployeeDetails();
	
	public String deleteEmployeeById(long employeeId);
	
	public Optional<EmployeeDTO> getEmployeeById(long employeeId);
	
	public Optional<EmployeeDTO> updateEmployeeById(long employeeId,Employee employee);
	
	public DepartmentDTO addDepartment(Department department);
	
	public   Department findByDepartmentName(String departmentName);
	
	public List<DepartmentDTO>allDepartment();
	
	
	

}
