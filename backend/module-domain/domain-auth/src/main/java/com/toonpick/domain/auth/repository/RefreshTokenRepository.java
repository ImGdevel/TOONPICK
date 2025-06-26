package com.toonpick.domain.auth.repository;

import org.springframework.data.repository.CrudRepository;
import com.toonpick.domain.auth.entity.RefreshToken;


public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    void deleteById(String token);

    Iterable<RefreshToken> findAll();
}
