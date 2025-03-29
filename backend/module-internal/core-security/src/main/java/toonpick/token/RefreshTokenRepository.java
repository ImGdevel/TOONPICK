package toonpick.token;

import org.springframework.data.repository.CrudRepository;


public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    void deleteByToken(String token);

    Iterable<RefreshToken> findAll();
}
