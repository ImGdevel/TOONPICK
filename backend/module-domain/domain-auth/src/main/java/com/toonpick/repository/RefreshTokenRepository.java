package com.toonpick.repository;

import org.springframework.data.repository.CrudRepository;
import com.toonpick.entity.RefreshToken;


public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    void deleteByToken(String token);

    Iterable<RefreshToken> findAll();
}
