package com.financeiro.user;

import java.util.Optional;

import com.financeiro.exceptions.RuntimeValidationException;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.financeiro.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

import static com.financeiro.utils.LoggerWrapper.*;

@Service
public class UserService {

    @Autowired
    public UserRepository repository;


    public Page<User> listAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        Page<User> users = repository.findAll(pageable);
        if (users.isEmpty()) {
            throw new RuntimeValidationException("Nenhum usuario cadastrado");
        }
        return users;
    }

    public Optional<User> findByLogin(String login) {
        return this.repository.findByLogin(login);
    }

    public User getById(Long id) {
        Optional<User> optionalUser = repository.findById(id);
        return optionalUser.orElse(null);
    }

    public User saveUser(User user) {
        validateUserUniqueFields(user.getEmail(), user.getLogin());

        try {
            encryptPassword(user);
            user.setActive(true);
            logInfo("Salvando usuário...");
            return repository.save(user);
        } catch (Exception e) {
            logError("Erro ao salvar usuário ", e);
            throw new ServiceException("Erro ao salvar usuário", e);
        }
    }

    public User updateUser(Long id, User updatedUser) {
        validateUserUniqueFields(updatedUser.getEmail(), updatedUser.getLogin());

        try {
            User user = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(id));
            updateUserData(user, updatedUser);
            return repository.save(user);
        } catch (ResourceNotFoundException e) {
            logError("Usuário não encontrado para atualização ", e);
            throw e;
        }
    }

    private void validateUserUniqueFields(String email, String login) {
        if (repository.findByEmail(email).isPresent()) {
            logWarn("Já existe um usuário cadastrado com esse e-mail");
            throw new RuntimeValidationException("Já existe um usuário cadastrado com esse e-mail");
        }
        if (repository.findByLogin(login).isPresent()) {
            logWarn("Já existe um usuário cadastrado com esse login");
            throw new RuntimeValidationException("Já existe um usuário cadastrado com esse login");
        }
    }

    private void encryptPassword(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    private void updateUserData(User user, User updatedUser) {
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setLogin(updatedUser.getLogin());
        user.setActive(updatedUser.isActive());
    }


    public User toggleActiveStatus() {
        User user = new User();
        user.setActive(!user.isActive());
        return repository.save(user);
    }
}