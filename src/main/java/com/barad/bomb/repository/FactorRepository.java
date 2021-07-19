package com.barad.bomb.repository;

import com.barad.bomb.domain.FactorEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FactorEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FactorRepository extends JpaRepository<FactorEntity, Long>, JpaSpecificationExecutor<FactorEntity> {}
