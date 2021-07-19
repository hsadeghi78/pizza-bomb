package com.barad.bomb.repository;

import com.barad.bomb.domain.FoodTypeEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FoodTypeEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FoodTypeRepository extends JpaRepository<FoodTypeEntity, Long>, JpaSpecificationExecutor<FoodTypeEntity> {}
