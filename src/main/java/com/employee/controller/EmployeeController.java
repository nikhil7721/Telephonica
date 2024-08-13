package com.employee.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.dto.DepartmentDTO;
import com.employee.dto.EmployeeDTO;
import com.employee.model.AuthenticationRequest;
import com.employee.model.AuthenticationResponse;
import com.employee.model.Department;
import com.employee.model.Employee;
import com.employee.serviceImpl.EmployeeServiceImpl;
import com.employee.serviceImpl.MyUserDetailsService;
import com.employee.util.JwtUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/employee")
@CrossOrigin("*")
public class EmployeeController {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private MyUserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	

	private final EmployeeServiceImpl employeeServiceImpl;

	public EmployeeController(EmployeeServiceImpl employeeServiceImpl) {
		this.employeeServiceImpl = employeeServiceImpl;
	}

	@PostMapping("/authenticate") // Authenticate a Customer (Existing)
	public ResponseEntity<?> generateToken(@RequestBody AuthenticationRequest authRequest) throws Exception {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
			);
		}catch (BadCredentialsException e) {
			throw new Exception("Invalid Username or Password!",e);
		}
		UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
		String token = jwtUtil.generateToken(userDetails);
		return ResponseEntity.ok(new AuthenticationResponse(token));
	}

	@PostMapping("/addemployee")
	public ResponseEntity<EmployeeDTO> addEmployee(@Valid @RequestBody Employee employee) {
		EmployeeDTO newEmployee = employeeServiceImpl.addEmployee(employee);
		return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
	}

	@GetMapping("/getallemployees")
	public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
		Optional<List<EmployeeDTO>> employees = employeeServiceImpl.getAllEmployeeDetails();
		if (employees.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(employees.get(), HttpStatus.OK);
	}

	@PutMapping("/updateemployee/{employeeId}")
	public ResponseEntity<EmployeeDTO> updateEmployeeById(@PathVariable long employeeId, @RequestBody Employee employee) {
		Optional<EmployeeDTO> updatedEmployee = employeeServiceImpl.updateEmployeeById(employeeId, employee);
		if (updatedEmployee.isPresent()) {
			return new ResponseEntity<>(updatedEmployee.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping("/getemployeebyid/{employeeId}")
	public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable("employeeId") long employeeId) {
		Optional<EmployeeDTO> optionalEmployee = employeeServiceImpl.getEmployeeById(employeeId);
		return optionalEmployee.map(employee -> ResponseEntity.ok(employee)).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/deleteemployee/{employeeId}")
	public ResponseEntity<String> deleteEmployeeById(@PathVariable long employeeId) {
		employeeServiceImpl.deleteEmployeeById(employeeId);
		return ResponseEntity.ok("Employee deleted successfully with id " + employeeId);

	}
	
	@GetMapping("/current-user")
	public Object getCurrentUser(Authentication authentication){
		return authentication.getPrincipal();
	}
	
	@PostMapping("/addDepartment")
	public ResponseEntity<DepartmentDTO> addDepartment(@RequestBody Department department){
		DepartmentDTO saveDepartment=employeeServiceImpl.addDepartment(department);
		return new ResponseEntity<>(saveDepartment,HttpStatus.CREATED);
		
	}
	
	@GetMapping("/allDepartments")
	public List<DepartmentDTO>allDepartments(){
		return employeeServiceImpl.allDepartment();	
	}

}
