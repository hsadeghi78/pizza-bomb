package com.barad.bomb.repository;

import com.barad.bomb.domain.AddressEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AddressEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long>, JpaSpecificationExecutor<AddressEntity> {}
