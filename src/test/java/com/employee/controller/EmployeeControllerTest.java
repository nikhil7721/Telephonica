package com.employee.controller;


import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.employee.TelephonicaEmployeeManagementApplication;
import com.employee.dto.DepartmentDTO;
import com.employee.dto.EmployeeDTO;
import com.employee.model.Department;
import com.employee.model.Employee;
import com.employee.repository.DepartmentRepository;
import com.employee.repository.EmployeeRepository;
import com.employee.serviceImpl.EmployeeServiceImpl;
import com.employee.serviceImpl.MyUserDetailsService;
import com.employee.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest(classes = {TelephonicaEmployeeManagementApplication.class})
class EmployeeControllerTest {

	   @Autowired
	    private MockMvc mockMvc;

	    @MockBean
	    private EmployeeServiceImpl employeeServiceImpl;
	    
	    @MockBean
	    private EmployeeRepository employeeRepository;
	    
	    @MockBean
	    private DepartmentRepository departmentRepository;

	    @MockBean
	    private AuthenticationManager authenticationManager;

	    @MockBean
	    private UserDetailsService userDetailsService;

	    @MockBean
	    private MyUserDetailsService myUserDetailsService;
	    
	    @MockBean
	    private JwtUtil jwtUtil;

	    @Autowired
	    private ObjectMapper objectMapper;
	    
	    @MockBean
	    private Employee employee;
	    
	    @MockBean
	    private EmployeeDTO employeeDTO;
	    

	    @BeforeEach
	    public void setup() {
	        objectMapper = new ObjectMapper();
	    }


	    
	    @Test
	    @WithMockUser
	    public void testAddEmployee() throws Exception {
	        Employee employee = new Employee();
	        EmployeeDTO employeeDTO = new EmployeeDTO();
	        employeeDTO.setEmployeeId(1L);

	        when(employeeServiceImpl.addEmployee(ArgumentMatchers.any(Employee.class))).thenReturn(employeeDTO);

	        mockMvc.perform(post("/employee/addemployee")
	                .contentType("application/json")
	                .content(objectMapper.writeValueAsString(employee)))
	                .andExpect(status().isCreated())
	                .andExpect(jsonPath("$.employeeId").exists());
	    }

	    @Test
	    @WithMockUser
	    public void testGetAllEmployees() throws Exception {
	        List<EmployeeDTO> employeeList = List.of(new EmployeeDTO());
	        when(employeeServiceImpl.getAllEmployeeDetails()).thenReturn(Optional.of(employeeList));

	        mockMvc.perform(get("/employee/getallemployees"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$").isArray());
	    }

	    @Test
	    @WithMockUser
	    public void testUpdateEmployeeById() throws Exception {
	        Employee employee = new Employee();
	        EmployeeDTO employeeDTO = new EmployeeDTO();
	        employeeDTO.setEmployeeId(1L);

	        when(employeeServiceImpl.updateEmployeeById(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Employee.class))).thenReturn(Optional.of(employeeDTO));

	        mockMvc.perform(put("/employee/updateemployee/{employeeId}", 1L)
	                .contentType("application/json")
	                .content(objectMapper.writeValueAsString(employee)))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.employeeId").exists());
	    }

	    @Test
	    @WithMockUser
	    public void testGetEmployeeById() throws Exception {
	        EmployeeDTO employeeDTO = new EmployeeDTO();
	        employeeDTO.setEmployeeId(1L);

	        when(employeeServiceImpl.getEmployeeById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(employeeDTO));

	        mockMvc.perform(get("/employee/getemployeebyid/{employeeId}", 1L))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.employeeId").exists());
	    }

	    @Test
	    @WithMockUser
	    public void testDeleteEmployeeById() throws Exception {
	       
	        when(employeeServiceImpl.deleteEmployeeById(1L)).thenReturn("Employee deleted successfully with id 1");
//	        when(employeeService.deleteEmployee(1L)).thenReturn("Employee deleted Successfully with This Id 1");

	        mockMvc.perform(delete("/employee/deleteemployee/{employeeId}", 1L))
	                .andExpect(status().isOk());
//	                .andExpect((ResultMatcher) content().string("Employee deleted successfully with id 1"));
	    }

	    @Test
	    @WithMockUser
	    public void testGetCurrentUser() throws Exception {
	        mockMvc.perform(get("/employee/current-user"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.username").exists());
	    }

	    @Test
	    @WithMockUser
	    public void testAddDepartment() throws Exception {
	        Department department = new Department();
	        DepartmentDTO departmentDTO = new DepartmentDTO();
	        departmentDTO.setDepartmentId(1L);

	        when(employeeServiceImpl.addDepartment(ArgumentMatchers.any(Department.class))).thenReturn(departmentDTO);

	        mockMvc.perform(post("/employee/addDepartment")
	                .contentType("application/json")
	                .content(objectMapper.writeValueAsString(department)))
	                .andExpect(status().isCreated())
	                .andExpect(jsonPath("$.departmentId").exists());
	    }

	    @Test
	    @WithMockUser
	    public void testAllDepartments() throws Exception {
	        List<DepartmentDTO> departmentList = List.of(new DepartmentDTO());

	        when(employeeServiceImpl.allDepartment()).thenReturn(departmentList);

	        mockMvc.perform(get("/employee/allDepartments"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$").isArray());
	    }
	    
//	    @Test
//	    public void testGenerateToken() throws Exception {
//	        AuthenticationRequest authRequest = new AuthenticationRequest("username", "password");
//
//	        // Mocking authentication and token generation
//	        UserDetails userDetails = new User("username", "password", new ArrayList<>());
//	        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken("username", "password");
//
//	        when(authenticationManager.authenticate(ArgumentMatchers.any(UsernamePasswordAuthenticationToken.class)))
//	                .thenReturn(authToken);
//	        when(userDetailsService.loadUserByUsername("username")).thenReturn(userDetails);
//	        when(jwtUtil.generateToken(userDetails)).thenReturn("dummyToken");
//
//	        mockMvc.perform(post("/employee/authenticate")
//	                .contentType("application/json")
//	                .content(objectMapper.writeValueAsString(authRequest)))
//	                .andExpect(status().isOk())
//	                .andExpect(jsonPath("$.token").value("dummyToken"));
//	    }
	}

