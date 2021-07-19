package com.barad.bomb.repository;

import com.barad.bomb.domain.FoodEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FoodEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FoodRepository extends JpaRepository<FoodEntity, Long>, JpaSpecificationExecutor<FoodEntity> {}
