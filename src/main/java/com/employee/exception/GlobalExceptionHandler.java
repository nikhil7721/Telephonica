package com.employee.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception exception, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(), exception.getMessage(),
				request.getDescription(false));

		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(EmployeeIdNotPresentException.class)
	public final ResponseEntity<ExceptionResponse> handleEmployeeIdNotPresentException(
			EmployeeIdNotPresentException employeeIdNotPresentException, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(),
				employeeIdNotPresentException.getMessage(), request.getDescription(false));

		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
	}

	public final ResponseEntity<ExceptionResponse> handleEmailIdAlreadyPresentException(
			EmailIdAlreadyPresentException emailIdAlreadyPresentException, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(),
				emailIdAlreadyPresentException.getMessage(), request.getDescription(false));
		
		return new ResponseEntity<>(exceptionResponse,HttpStatus.ALREADY_REPORTED);
	}

//	@ExceptionHandler(EmployeeIdNotPresentException.class)
//	public ResponseEntity<String>handleEmployeeIdNotPresent(EmployeeIdNotPresentException ex){
//		return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
//	}
//	

}
