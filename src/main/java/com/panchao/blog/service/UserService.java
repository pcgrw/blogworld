package com.panchao.blog.service;

import com.panchao.blog.model.entity.User;

/**
 * User Service
 */
public interface UserService {
    User save(User user);

    User checkUser(String username, String password);
}
