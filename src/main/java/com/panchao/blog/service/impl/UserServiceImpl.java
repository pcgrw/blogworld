package com.panchao.blog.service.impl;

import com.panchao.blog.model.entity.User;
import com.panchao.blog.repository.UserRepository;
import com.panchao.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * User Service Impl
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User checkUser(String username, String password) {
        Optional<User> optional = userRepository.findByUsernameAndAndPassword(username, password);
        return optional.orElse(null);
    }
}
