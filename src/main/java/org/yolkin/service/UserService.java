package org.yolkin.service;

import org.yolkin.model.File;
import org.yolkin.model.User;
import org.yolkin.repository.UserRepository;
import org.yolkin.repository.hibernate.HibernateUserRepositoryImpl;

import java.util.List;

public class UserService {
    private final UserRepository userRepository;

    public UserService() {
        userRepository = new HibernateUserRepositoryImpl();
    }

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        return userRepository.getAll();
    }

    public User create(User user) {
        return userRepository.create(user);
    }

    public User getById(Long id) {
        return userRepository.getById(id);
    }

    public User update(User user) {
        return userRepository.update(user);
    }

    public void delete(Long id) {
        userRepository.delete(id);
    }
}