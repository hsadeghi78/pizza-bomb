package com.barad.bomb.repository;

import com.barad.bomb.domain.CriticismEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CriticismEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CriticismRepository extends JpaRepository<CriticismEntity, Long>, JpaSpecificationExecutor<CriticismEntity> {}
