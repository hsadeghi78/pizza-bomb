package com.barad.bomb.repository;

import com.barad.bomb.domain.CommentEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CommentEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long>, JpaSpecificationExecutor<CommentEntity> {}
