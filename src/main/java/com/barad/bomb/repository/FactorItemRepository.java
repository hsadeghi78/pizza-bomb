package com.barad.bomb.repository;

import com.barad.bomb.domain.FactorItemEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FactorItemEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FactorItemRepository extends JpaRepository<FactorItemEntity, Long>, JpaSpecificationExecutor<FactorItemEntity> {}
