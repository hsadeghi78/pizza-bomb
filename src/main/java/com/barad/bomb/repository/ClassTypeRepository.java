package com.barad.bomb.repository;

import com.barad.bomb.domain.ClassTypeEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ClassTypeEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassTypeRepository extends JpaRepository<ClassTypeEntity, Long>, JpaSpecificationExecutor<ClassTypeEntity> {}
