package com.financeiro.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;



/***
 * Esta classe é responsável por lidar com exceções de validação
 * durante a validação de entradas nos controllers.
 *
 * Ao utilizar a anotação @RestControllerAdvice, a classe se torna
 * um handler global para exceções em todos os controllers da aplicação.
 *
 * O método handleValidationExceptions é anotado com @ExceptionHandler
 * e lida especificamente com exceções do tipo MethodArgumentNotValidException,
 * que ocorrem quando a validação de uma entrada falha de acordo com as
 * regras definidas nas entidades ou nos DTOs.
 *
 * O método captura as mensagens de erro de validação, organiza-as em um
 * mapa onde a chave é o nome do campo e o valor é a mensagem de erro,
 * e retorna uma resposta HTTP com o código de status 400 (Bad Request)
 * e o corpo contendo um mapa com a chave "error" e o mapa de erros.
 */


@RestControllerAdvice
public class ValidationExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, Object> errorResponse = new HashMap<>();
	    Map<String, String> errors = new HashMap<>();
	    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
	        errors.put(error.getField(), error.getDefaultMessage());
	    }
	    errorResponse.put("error", errors);
	    return ResponseEntity.badRequest().body(errorResponse);
	}

}