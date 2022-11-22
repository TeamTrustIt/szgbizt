package hu.bme.szgbizt.secushop.persistence.repository;

import hu.bme.szgbizt.secushop.persistence.entity.CaffDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CaffDataRepository extends JpaRepository<CaffDataEntity, UUID> {
    // Empty.
}
