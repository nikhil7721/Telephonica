package com.employee.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.employee.dto.DepartmentDTO;
import com.employee.dto.EmployeeDTO;
import com.employee.exception.EmployeeIdNotPresentException;
import com.employee.mapper.EmployeeMapper;
import com.employee.model.Department;
import com.employee.model.Employee;
import com.employee.repository.DepartmentRepository;
import com.employee.repository.EmployeeRepository;
import com.employee.service.EmployeeService;

import jakarta.transaction.Transactional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository employeeRepository;

	private final DepartmentRepository departmentRepository;

	private final BCryptPasswordEncoder passwordEncoder;
	
	
	private final EmployeeMapper employeeMapper;

	

	public EmployeeServiceImpl(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository,
			BCryptPasswordEncoder passwordEncoder, EmployeeMapper employeeMapper) {
		super();
		this.employeeRepository = employeeRepository;
		this.departmentRepository = departmentRepository;
		this.passwordEncoder = passwordEncoder;
		this.employeeMapper = employeeMapper;
	}

	private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

	@Override
	public EmployeeDTO addEmployee(Employee employee) {

		logger.info("Starting task Execution.");
		if (employeeRepository.existsByEmployeeEmail(employee.getEmployeeEmail())) {
			throw new RuntimeException("Email already exists");
		}

		Optional<Department> department = Optional
				.ofNullable((departmentRepository.findByDepartmentName(employee.getDepartment().getDepartmentName())));
		if (!department.isPresent()) {
			throw new IllegalArgumentException("Department does not exist");
		}

		employee.setDepartment(department.get());
		employee.setPassword(passwordEncoder.encode(employee.getPassword()));
		Employee savedEmployee = employeeRepository.save(employee);

		logger.info("task execution completed");
		EmployeeDTO saveEmployee = employeeMapper.toDTO(savedEmployee);
		return saveEmployee;

	}

	@Override
	public Optional<List<EmployeeDTO>> getAllEmployeeDetails() {
		logger.info("Starting task Execution.");
		List<Employee> allEmployee = employeeRepository.findAll();
		List<EmployeeDTO> allEmployees = allEmployee.stream().map(employeeMapper::toDTO).collect(Collectors.toList());
		logger.info("Task execution completed.");
		return Optional.ofNullable(allEmployees);
	}

	@Transactional
	@Override
	public String deleteEmployeeById(long employeeId) {
		logger.info("Starting task Execution.");
		employeeRepository.findById(employeeId).orElseThrow(
				() -> new EmployeeIdNotPresentException("Employee Id Not Found using this id  " + employeeId));
		employeeRepository.deleteById(employeeId);
		logger.info("Task execution completed.");
		return "Employee deleted successfully with id " + employeeId;
	}

	@Override
	public Optional<EmployeeDTO> getEmployeeById(long employeeId) {
		logger.info("Starting task Execution.");
		Employee  employee = employeeRepository.findById(employeeId).orElseThrow(
				() -> new EmployeeIdNotPresentException("Employee Id Not Found using this id  " + employeeId));
		EmployeeDTO getEmployee = employeeMapper.toDTO(employee);
		logger.info("Task execution completed.");
		return Optional.ofNullable(getEmployee);
	}

	@Override
	public Optional<EmployeeDTO> updateEmployeeById(long employeeId, Employee employee) {
		logger.info("Starting task Execution.");
		var existingEmployee=employeeRepository.findById(employeeId).orElseThrow(
				() -> new EmployeeIdNotPresentException("Employee Id Not Found using this id   " + employeeId));

		existingEmployee.setEmployeeName(employee.getEmployeeName());
		existingEmployee.setEmployeeEmail(employee.getEmployeeEmail());
		existingEmployee.setUsername(employee.getUsername());
		existingEmployee.setPassword(employee.getPassword());
		existingEmployee.setDepartment(employee.getDepartment());
		Employee savedEmployee =employeeRepository.save(existingEmployee);
		EmployeeDTO updatedEmployee = employeeMapper.toDTO(savedEmployee);
		logger.info("Task execution completed.");

		return Optional.ofNullable(updatedEmployee);
	}

	public Employee getEmployeeByEmail(String employeeEmail) {
		return employeeRepository.findByEmployeeEmail(employeeEmail);
	}

	@Override
	public DepartmentDTO addDepartment(Department department) {
		departmentRepository.save(department);
		DepartmentDTO departmentDTO = new DepartmentDTO();
		departmentDTO.setDepartmentId(department.getDepartmentId());
		departmentDTO.setDepartmentName(department.getDepartmentName());
		return departmentDTO;
	}

	@Override
	public Department findByDepartmentName(String departmentName) {
		Department department = departmentRepository.findByDepartmentName(departmentName);
		return department;
	}

	@Override
	public List<DepartmentDTO> allDepartment() {
		List<Department> allDepartments = departmentRepository.findAll();
		List<DepartmentDTO> allDepartmentss = new ArrayList<>();
	
		allDepartments.stream().forEach((p) -> {
			DepartmentDTO department = new DepartmentDTO();
			department.setDepartmentId(p.getDepartmentId());
			department.setDepartmentName(p.getDepartmentName());
			allDepartmentss.add(department);
		});

		return allDepartmentss;
	}

}
