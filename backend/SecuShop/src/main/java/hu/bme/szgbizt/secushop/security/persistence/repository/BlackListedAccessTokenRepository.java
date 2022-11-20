package hu.bme.szgbizt.secushop.security.persistence.repository;

import hu.bme.szgbizt.secushop.security.persistence.entity.BlackListedAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BlackListedAccessTokenRepository extends JpaRepository<BlackListedAccessToken, UUID> {

    Optional<BlackListedAccessToken> findByToken(String token);
}
