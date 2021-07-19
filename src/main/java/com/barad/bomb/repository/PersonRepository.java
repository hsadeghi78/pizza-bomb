package com.barad.bomb.repository;

import com.barad.bomb.domain.PersonEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PersonEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Long>, JpaSpecificationExecutor<PersonEntity> {}
