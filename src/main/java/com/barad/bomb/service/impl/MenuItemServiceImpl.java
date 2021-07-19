package com.barad.bomb.service.impl;

import com.barad.bomb.domain.MenuItemEntity;
import com.barad.bomb.repository.MenuItemRepository;
import com.barad.bomb.service.MenuItemService;
import com.barad.bomb.service.dto.MenuItemDTO;
import com.barad.bomb.service.mapper.MenuItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MenuItemEntity}.
 */
@Service
@Transactional
public class MenuItemServiceImpl implements MenuItemService {

    private final Logger log = LoggerFactory.getLogger(MenuItemServiceImpl.class);

    private final MenuItemRepository menuItemRepository;

    private final MenuItemMapper menuItemMapper;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository, MenuItemMapper menuItemMapper) {
        this.menuItemRepository = menuItemRepository;
        this.menuItemMapper = menuItemMapper;
    }

    @Override
    public MenuItemDTO save(MenuItemDTO menuItemDTO) {
        log.debug("Request to save MenuItem : {}", menuItemDTO);
        MenuItemEntity menuItemEntity = menuItemMapper.toEntity(menuItemDTO);
        menuItemEntity = menuItemRepository.save(menuItemEntity);
        return menuItemMapper.toDto(menuItemEntity);
    }

    @Override
    public Optional<MenuItemDTO> partialUpdate(MenuItemDTO menuItemDTO) {
        log.debug("Request to partially update MenuItem : {}", menuItemDTO);

        return menuItemRepository
            .findById(menuItemDTO.getId())
            .map(
                existingMenuItem -> {
                    menuItemMapper.partialUpdate(existingMenuItem, menuItemDTO);
                    return existingMenuItem;
                }
            )
            .map(menuItemRepository::save)
            .map(menuItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MenuItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MenuItems");
        return menuItemRepository.findAll(pageable).map(menuItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MenuItemDTO> findOne(Long id) {
        log.debug("Request to get MenuItem : {}", id);
        return menuItemRepository.findById(id).map(menuItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MenuItem : {}", id);
        menuItemRepository.deleteById(id);
    }
}
