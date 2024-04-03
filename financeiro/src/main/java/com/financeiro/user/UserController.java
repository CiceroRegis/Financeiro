package com.financeiro.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@Validated
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/v1")
@Tag(name = "User", description = "Api user controller")
public class UserController {

	private static final String SUCCESS = "sucesso";
	private static final String MESSAGE = "mensagem";

	@Autowired
	private UserService userService;

	@PostMapping("list/users")
	public ResponseEntity<HashMap<String, Object>> listlAll() {

		HashMap<String, Object> response = new HashMap<>();
		List<User> users = new ArrayList<User>();
		try {
			users = userService.listAll();
		} catch (EntityNotFoundException e) {
			response.put(SUCCESS, false);
			response.put(MESSAGE, e.getMessage());
			return ResponseEntity.status((HttpStatus) HttpStatus.NOT_FOUND).body(response);
		} catch (ServiceException e) {
			response.put(SUCCESS, false);
			response.put(MESSAGE, e.getMessage());
			return ResponseEntity.status((HttpStatus) HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		response.put(SUCCESS, true);
		response.put("Lista de usuarios: ", users);
		return ResponseEntity.ok().body(response);
	}

	@PostMapping("save/user")
	public ResponseEntity<Object> saveUser(@Valid @RequestBody User user) {
		HashMap<String, Object> response = new HashMap<>();

		try {
			user = userService.saveUser(user);
		} catch (EntityNotFoundException e) {
			response.put(SUCCESS, false);
			response.put(MESSAGE, e.getMessage());
			return ResponseEntity.status((HttpStatus) HttpStatus.NOT_FOUND).body(response);
		} catch (ServiceException e) {
			response.put(SUCCESS, false);
			response.put(MESSAGE, e.getMessage());
			return ResponseEntity.status((HttpStatus) HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		response.put(SUCCESS, true);
		response.put(MESSAGE, "Usuário cadastrado com sucesso!");
		response.put("Usuário casdastrado", user);
		return ResponseEntity.ok().body(response);
	}
	
	@PutMapping(value = "updateUser/user/{id}")
	public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody User user) {

		HashMap<String, Object> response = new HashMap<>();
		try {
			user = userService.updateUser(id, user);
		} catch (EntityNotFoundException e) {
			response.put(SUCCESS, false);
			response.put(MESSAGE, e.getMessage());
			return ResponseEntity.status((HttpStatus) HttpStatus.NOT_FOUND).body(response);
		} catch (ServiceException e) {
			response.put(SUCCESS, false);
			response.put(MESSAGE, e.getMessage());
			return ResponseEntity.status((HttpStatus) HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		response.put(SUCCESS, true);
		response.put(MESSAGE, "Dados atualizados com sucesso!");
		response.put("Dados atualizados", user);
		return ResponseEntity.ok().body(response);

	}

	@PostMapping("toggle-active-status/user/{id}")
    public ResponseEntity<HashMap<String, Object>> toggleActiveStatus(@PathVariable Long id, @RequestBody User user) {
		HashMap<String, Object> response = new HashMap<>();
		user.setActive(!user.isActive());
	    userService.saveUser(user);

	    response.put(SUCCESS, true);
	    response.put(MESSAGE, "Status do usuário atualizado com sucesso");
	    response.put("Status atualizado", user.isActive());
	    return ResponseEntity.ok().body(response);
    }
}
