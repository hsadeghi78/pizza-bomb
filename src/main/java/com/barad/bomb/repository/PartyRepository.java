package com.barad.bomb.repository;

import com.barad.bomb.domain.PartyEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PartyEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PartyRepository extends JpaRepository<PartyEntity, Long>, JpaSpecificationExecutor<PartyEntity> {}
