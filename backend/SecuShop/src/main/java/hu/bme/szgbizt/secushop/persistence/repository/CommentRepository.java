package hu.bme.szgbizt.secushop.persistence.repository;

import hu.bme.szgbizt.secushop.persistence.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, UUID> {
    // Empty.
}
