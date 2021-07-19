package com.barad.bomb.repository;

import com.barad.bomb.domain.ContactEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ContactEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, Long>, JpaSpecificationExecutor<ContactEntity> {}
