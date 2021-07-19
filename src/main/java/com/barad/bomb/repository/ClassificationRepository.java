package com.barad.bomb.repository;

import com.barad.bomb.domain.ClassificationEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ClassificationEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassificationRepository
    extends JpaRepository<ClassificationEntity, Long>, JpaSpecificationExecutor<ClassificationEntity> {}
