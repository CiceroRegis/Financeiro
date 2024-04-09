package com.financeiro.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
	private static final String MESSAGE = "message";

	@Autowired
	private UserService userService;

	@GetMapping("list/users")
	public ResponseEntity<HashMap<String, Object>> listAll(@RequestParam(defaultValue = "0") int page,
														   @RequestParam(defaultValue = "10") int size) {
		HashMap<String, Object> response = new HashMap<>();
		try {
			Page<User> usersPage = userService.listAll(page, size);
			List<User> users = usersPage.getContent();
			response.put(SUCCESS, true);
			response.put("users", users);
			response.put("total_usuários", usersPage.getTotalElements());
			response.put("total_páginas", usersPage.getTotalPages());
			return ResponseEntity.ok().body(response);
		} catch (EntityNotFoundException e) {
			response.put(SUCCESS, false);
			response.put(MESSAGE, e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		} catch (ServiceException e) {
			response.put(SUCCESS, false);
			response.put(MESSAGE, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
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



	@GetMapping(value = "getById/user/{id}")
	public ResponseEntity<Object> getById(@PathVariable Long id) {

		HashMap<String, Object> response = new HashMap<>();
		User user = new User();
		try {
			user = userService.getById(id);
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
		response.put("user", user);
		return ResponseEntity.ok().body(response);

	}

}
