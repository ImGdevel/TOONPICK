package com.toonpick.app.repository;

import com.toonpick.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByName(String name);
    User findByName(String name);
}
