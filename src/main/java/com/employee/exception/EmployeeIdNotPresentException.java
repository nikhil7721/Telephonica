package com.employee.exception;

public class EmployeeIdNotPresentException  extends RuntimeException{
	
	public EmployeeIdNotPresentException(String msg) {
		super(msg);
	}

}
