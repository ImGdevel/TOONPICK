package com.toonpick.repository;

import org.springframework.data.repository.CrudRepository;
import com.toonpick.entity.RefreshToken;


public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    void deleteById(String token);

    Iterable<RefreshToken> findAll();
}
