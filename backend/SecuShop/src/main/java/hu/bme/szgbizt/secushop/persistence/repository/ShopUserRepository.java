package hu.bme.szgbizt.secushop.persistence.repository;

import hu.bme.szgbizt.secushop.persistence.entity.ShopUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShopUserRepository extends JpaRepository<ShopUserEntity, UUID> {
    // Empty.
}
