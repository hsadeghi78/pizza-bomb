package com.barad.bomb.repository;

import com.barad.bomb.domain.PartyInformationEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PartyInformationEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PartyInformationRepository
    extends JpaRepository<PartyInformationEntity, Long>, JpaSpecificationExecutor<PartyInformationEntity> {}
