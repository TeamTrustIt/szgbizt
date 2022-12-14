package hu.bme.szgbizt.secushop.security.persistence.repository;

import hu.bme.szgbizt.secushop.security.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findByRolesIs(String roles);
}
