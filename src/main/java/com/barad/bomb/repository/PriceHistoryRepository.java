package com.barad.bomb.repository;

import com.barad.bomb.domain.PriceHistoryEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PriceHistoryEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistoryEntity, Long>, JpaSpecificationExecutor<PriceHistoryEntity> {}
