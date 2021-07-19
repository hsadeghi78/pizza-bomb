package com.barad.bomb.repository;

import com.barad.bomb.domain.ConsumeMaterialEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ConsumeMaterialEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConsumeMaterialRepository
    extends JpaRepository<ConsumeMaterialEntity, Long>, JpaSpecificationExecutor<ConsumeMaterialEntity> {}
