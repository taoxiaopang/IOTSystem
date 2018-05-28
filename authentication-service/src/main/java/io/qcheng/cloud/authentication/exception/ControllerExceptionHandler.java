package io.qcheng.cloud.authentication.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(value = "com.qcheng.cloud.server.controller")
public class ControllerExceptionHandler {
	private MessageSource messageSource;
	
	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<List<FieldValidationError>> handleValidationError(
			MethodArgumentNotValidException validException) {
		
		List<FieldValidationError> errorList = new ArrayList<>();
		for(FieldError error : validException.getBindingResult().getFieldErrors()) {
			errorList.add(
					new FieldValidationError(error.getField(), 
							messageSource.getMessage(error.getDefaultMessage(), null, LocaleContextHolder.getLocale())));
		}
		
		return new ResponseEntity<>(errorList, HttpStatus.BAD_REQUEST);
	}


}
