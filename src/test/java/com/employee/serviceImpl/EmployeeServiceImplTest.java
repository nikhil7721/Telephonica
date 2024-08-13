package com.employee.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.employee.dto.DepartmentDTO;
import com.employee.dto.EmployeeDTO;
import com.employee.mapper.EmployeeMapper;
import com.employee.model.Department;
import com.employee.model.Employee;
import com.employee.repository.DepartmentRepository;
import com.employee.repository.EmployeeRepository;

class EmployeeServiceImplTest {

	@Mock
	private EmployeeRepository employeeRepository;

	@Mock
	private DepartmentRepository departmentRepository;

	@Mock
	private BCryptPasswordEncoder passwordEncoder;

	@InjectMocks
	private EmployeeServiceImpl employeeServiceImpl;

	@Mock
	private Employee employee;

	@Mock
	private EmployeeDTO employeeDTO;

	@Mock
	private EmployeeMapper employeeMapper;

	@Mock
	private Department department;

	@Mock
	private DepartmentDTO departmentDTO;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testAddEmployee() {
		Department department = new Department();
		department.setDepartmentName("IT");

		Employee employee = new Employee();
		employee.setEmployeeEmail("nikhil@example.com");
		employee.setDepartment(department);
		employee.setPassword("password");

		Employee savedEmployee = new Employee();
		savedEmployee.setEmployeeEmail("nikhil@example.com");
		savedEmployee.setDepartment(department);
		savedEmployee.setPassword("encodedPassword");

		EmployeeDTO employeeDTO = new EmployeeDTO();
		employeeDTO.setEmployeeEmail("nikhil@example.com");
		employeeDTO.setPassword("encodedPassword");

		when(departmentRepository.findByDepartmentName("IT")).thenReturn(department);
		when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
		when(employeeRepository.save(employee)).thenReturn(savedEmployee);
		when(employeeMapper.toDTO(savedEmployee)).thenReturn(employeeDTO);

		EmployeeDTO result = employeeServiceImpl.addEmployee(employee);

		assertNotNull(result);
		assertEquals("encodedPassword", result.getPassword());
		verify(employeeRepository).save(employee);
	}

	@Test
	void testGetAllEmployeeDetails() {
		Department department = new Department();
		department.setDepartmentId(1L);
		department.setDepartmentName("IT");

		DepartmentDTO departmentDTO = new DepartmentDTO();
		departmentDTO.setDepartmentId(1L);
		departmentDTO.setDepartmentName("IT");

		Employee employee = new Employee();
		employee.setEmployeeId(1L);
		employee.setEmployeeName("Nikhil Patil");
		employee.setDepartment(department);

		EmployeeDTO employeeDTO = new EmployeeDTO();
		employeeDTO.setEmployeeId(1L);
		employeeDTO.setEmployeeName("Nikhil Patil");
		employeeDTO.setDepartment(departmentDTO);

		when(employeeRepository.findAll()).thenReturn(List.of(employee));
		when(employeeMapper.toDTO(employee)).thenReturn(employeeDTO);

		Optional<List<EmployeeDTO>> employees = employeeServiceImpl.getAllEmployeeDetails();

		assertTrue(employees.isPresent());
		assertFalse(employees.get().isEmpty());
		assertEquals(1L, employees.get().get(0).getEmployeeId());
		assertEquals("Nikhil Patil", employees.get().get(0).getEmployeeName());
		assertEquals(1L, employees.get().get(0).getDepartment().getDepartmentId());
		assertEquals("IT", employees.get().get(0).getDepartment().getDepartmentName());
	}

	@Test
	void testDeleteEmployeeById() {
		long employeeId = 1L;

		when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(new Employee()));
		doNothing().when(employeeRepository).deleteById(employeeId);

		String response = employeeServiceImpl.deleteEmployeeById(employeeId);

		assertEquals("Employee deleted successfully with id " + employeeId, response);
	}

	@Test
	void testGetEmployeeById() {
		long employeeId = 1L;
		Department department = new Department();
		department.setDepartmentId(1L);
		department.setDepartmentName("IT");

		Employee employee = new Employee();
		employee.setEmployeeId(employeeId);
		employee.setEmployeeName("Nikhil Patil");
		employee.setDepartment(department);

		DepartmentDTO departmentDTO = new DepartmentDTO();
		departmentDTO.setDepartmentId(1L);
		departmentDTO.setDepartmentName("IT");

		EmployeeDTO employeeDTO = new EmployeeDTO();
		employeeDTO.setEmployeeId(employeeId);
		employeeDTO.setEmployeeName("Nikhil Patil");
		employeeDTO.setDepartment(departmentDTO);

		when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
		when(employeeMapper.toDTO(employee)).thenReturn(employeeDTO);

		Optional<EmployeeDTO> result = employeeServiceImpl.getEmployeeById(employeeId);

		assertTrue(result.isPresent());
		assertEquals("Nikhil Patil", result.get().getEmployeeName());
		assertEquals(1L, result.get().getEmployeeId());
		assertEquals(1L, result.get().getDepartment().getDepartmentId());
		assertEquals("IT", result.get().getDepartment().getDepartmentName());
	}

	@Test
	void testUpdateEmployeeById() {
		long employeeId = 1L;
		Employee existingEmployee = new Employee();
		existingEmployee.setEmployeeName("Nikhil Patil");

		Department department = new Department();
		department.setDepartmentId(1L);
		existingEmployee.setDepartment(department);

		Employee updatedEmployee = new Employee();
		updatedEmployee.setEmployeeName("Nikhil Patil");

		EmployeeDTO updatedEmployeeDTO = new EmployeeDTO();
		updatedEmployeeDTO.setEmployeeName("Nikhil Patil");

		when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
		when(employeeRepository.save(existingEmployee)).thenReturn(existingEmployee);
		when(employeeMapper.toDTO(existingEmployee)).thenReturn(updatedEmployeeDTO);

		var result = employeeServiceImpl.updateEmployeeById(employeeId, updatedEmployee);

		assertTrue(result.isPresent());
		assertEquals("Nikhil Patil", result.get().getEmployeeName());
	}

	@Test
	void testAddDepartment() {
		Department department = new Department();
		department.setDepartmentName("HR");

		DepartmentDTO departmentDTO = new DepartmentDTO();
		departmentDTO.setDepartmentName("HR");

		when(departmentRepository.save(department)).thenReturn(department);

		DepartmentDTO result = employeeServiceImpl.addDepartment(department);

		assertNotNull(result);
		assertEquals("HR", result.getDepartmentName());
	}

	@Test
	void testAllDepartment() {
		Department department = new Department();
		department.setDepartmentName("Finance");

		when(departmentRepository.findAll()).thenReturn(List.of(department));

		List<DepartmentDTO> departments = employeeServiceImpl.allDepartment();

		assertNotNull(departments);
		assertFalse(departments.isEmpty());
		assertEquals("Finance", departments.get(0).getDepartmentName());
	}

}
