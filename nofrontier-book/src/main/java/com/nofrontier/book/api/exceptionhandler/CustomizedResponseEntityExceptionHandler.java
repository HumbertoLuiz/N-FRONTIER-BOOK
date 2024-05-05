package com.nofrontier.book.api.exceptionhandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.nofrontier.book.domain.exceptions.ExceptionResponse;
import com.nofrontier.book.domain.exceptions.InvalidJwtAuthenticationException;
import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.exceptions.ResourceNotFoundException;
import com.nofrontier.book.domain.exceptions.TokenServiceException;
import com.nofrontier.book.domain.exceptions.ValidatingException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice(annotations = RestController.class)
public class CustomizedResponseEntityExceptionHandler
		extends
			ResponseEntityExceptionHandler {

	private SnakeCaseStrategy camelCaseToSnakeCase = new SnakeCaseStrategy();

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ExceptionResponse> handleAllExceptions(
			Exception ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(),
				ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse,
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public final ResponseEntity<ExceptionResponse> handleNotFoundExceptions(
			Exception ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(),
				ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(RequiredObjectIsNullException.class)
	public final ResponseEntity<ExceptionResponse> handleBadRequestExceptions(
			Exception ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(),
				ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ValidatingException.class)
	public ResponseEntity<Object> handleValidacaoException(
			ValidatingException exception) {
		var body = new HashMap<String, List<String>>();
		var fieldError = exception.getFieldError();
		var fieldErrors = new ArrayList<String>();
		fieldErrors.add(fieldError.getDefaultMessage());
		var field = camelCaseToSnakeCase.translate(fieldError.getField());
		body.put(field, fieldErrors);
		return ResponseEntity.badRequest().body(body);
	}

	@ExceptionHandler(InvalidJwtAuthenticationException.class)
	public final ResponseEntity<ExceptionResponse> handleInvalidJwtAuthenticationExceptions(
			Exception ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(),
				ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(TokenServiceException.class)
	public ResponseEntity<Object> handleTokenServiceException(
			TokenServiceException exception, HttpServletRequest request) {
		return createErrorResponse(HttpStatus.UNAUTHORIZED,
				exception.getLocalizedMessage(), request.getRequestURI());
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<Object> handleEntityNotFoundException(
			EntityNotFoundException exception, HttpServletRequest request) {
		return createErrorResponse(HttpStatus.NOT_FOUND,
				exception.getLocalizedMessage(), request.getRequestURI());
	}

	public ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException exception,
			HttpServletRequest request) {
		return handleBindException(exception, request);
	}

	public ResponseEntity<Object> handleBindException(BindException exception,
			HttpServletRequest request) {
		var body = new HashMap<String, List<String>>();
		exception.getBindingResult().getFieldErrors().forEach(fieldError -> {
			var field = camelCaseToSnakeCase.translate(fieldError.getField());
			if (!body.containsKey(field)) {
				var fieldErrors = new ArrayList<String>();
				fieldErrors.add(fieldError.getDefaultMessage());
				body.put(field, fieldErrors);
			} else {
				body.get(field).add(fieldError.getDefaultMessage());
			}
		});
		return ResponseEntity.badRequest().body(body);
	}

	private ResponseEntity<Object> createErrorResponse(HttpStatus status,
			String message, String path) {
		Map<String, Object> body = new HashMap<>();
		body.put("status", status.value());
		body.put("error", status.getReasonPhrase());
		body.put("message", message);
		body.put("path", path);
		return new ResponseEntity<>(body, status);
	}
}