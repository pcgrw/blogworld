package com.panchao.blog.repository;

import com.panchao.blog.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * User Repository
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameAndAndPassword(String username, String password);
}
