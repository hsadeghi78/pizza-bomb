package com.barad.bomb.service;

import com.barad.bomb.service.dto.MenuItemDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.barad.bomb.domain.MenuItemEntity}.
 */
public interface MenuItemService {
    /**
     * Save a menuItem.
     *
     * @param menuItemDTO the entity to save.
     * @return the persisted entity.
     */
    MenuItemDTO save(MenuItemDTO menuItemDTO);

    /**
     * Partially updates a menuItem.
     *
     * @param menuItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MenuItemDTO> partialUpdate(MenuItemDTO menuItemDTO);

    /**
     * Get all the menuItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MenuItemDTO> findAll(Pageable pageable);

    /**
     * Get the "id" menuItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MenuItemDTO> findOne(Long id);

    /**
     * Delete the "id" menuItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
