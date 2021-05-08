package com.barad.bomb.repository;

import com.barad.bomb.domain.BranchEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BranchEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BranchRepository extends JpaRepository<BranchEntity, Long>, JpaSpecificationExecutor<BranchEntity> {}
