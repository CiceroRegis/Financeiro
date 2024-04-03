package com.financeiro.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.financeiro.exceptions.ResourceNotFoundException;
import com.financeiro.utils.LoggerWrapper;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

	@Autowired
	public UserRepository repository;

	public List<User> listAll() {
		List<User> user = new ArrayList<User>();
		try {
			user = repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
			if (user.isEmpty()){
				LoggerWrapper.logWarn("A lista de usuarios esta vazia");
			}
		} catch (Exception e) {
			LoggerWrapper.logError("Erro ao lista usuario", e);
			throw new ServiceException("Erro ao lista usuario", e);
		}
		return user;
	}

	public Optional<User> findByLogin(String login) {
		return this.repository.findByLogin(login);
	}

	public User saveUser(User user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		try {
			LoggerWrapper.logInfo("Incriptando Senha");
			user.setPassword(encoder.encode(user.getPassword()));
			user.setActive(true);
			LoggerWrapper.logInfo("Salvando user...");
			repository.save(user);
			LoggerWrapper.logInfo("Usuário salvo com sucesso");
		} catch (Exception e) {
			LoggerWrapper.logError("Erro ao salvar usuario ", e);
			throw new ServiceException("Erro ao salvar usuario", e);
		}
		return user;
	}

	public User updateUser(Long id, User obj) {

		try {

			User user = repository.getReferenceById(id);
			dataEdit(user, obj);
			return repository.save(user);
		} catch (EntityNotFoundException e) {
			LoggerWrapper.logError("Erro ao editar usuario ", e);
			throw new ResourceNotFoundException(id);
		}
	}

	private void dataEdit(User user, User obj) {
		user.setName(obj.getName());
		user.setEmail(obj.getEmail());
		user.setLogin(obj.getLogin());
		user.setPassword(obj.getPassword());
		user.setActive(obj.isActive());
	}

	public User toggleActiveStatus() {
	    User user = new User();
	    user.setActive(!user.isActive());
	    return repository.save(user);
	}

}
