package com.barad.bomb.repository;

import com.barad.bomb.domain.FactorStatusHistoryEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FactorStatusHistoryEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FactorStatusHistoryRepository
    extends JpaRepository<FactorStatusHistoryEntity, Long>, JpaSpecificationExecutor<FactorStatusHistoryEntity> {}
