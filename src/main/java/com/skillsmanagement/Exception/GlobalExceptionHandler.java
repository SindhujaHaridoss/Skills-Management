package com.skillsmanagement.Exception;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.skillsmanagement.helper.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> CustomException(RuntimeException ex) {
		logger.error("Runtime Exception block"+ex.getMessage());
		Map<String, Object> responseMap = new HashMap<>();
		ResponseStatus status = new ResponseStatus();
		status.setStatusCode("500");
		status.setStatusDescription("Internal Service Error");
		status.setStatusType("Error");
		responseMap.put("Status", status);

		return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
	}


	private <E extends Enum<E>> Map<String, String> getValidEnumValues(Class<E> enumClass) {
		return Arrays.stream(enumClass.getEnumConstants())
				.collect(Collectors.toMap(Enum::name, Enum::name));
	}

}
