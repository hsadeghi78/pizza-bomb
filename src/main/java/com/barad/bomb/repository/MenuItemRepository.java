package com.barad.bomb.repository;

import com.barad.bomb.domain.MenuItemEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MenuItemEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MenuItemRepository extends JpaRepository<MenuItemEntity, Long>, JpaSpecificationExecutor<MenuItemEntity> {}
