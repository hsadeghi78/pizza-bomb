package com.barad.bomb.service.mapper;

import com.barad.bomb.domain.*;
import com.barad.bomb.service.dto.MenuItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MenuItemEntity} and its DTO {@link MenuItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { PartyMapper.class, FoodMapper.class })
public interface MenuItemMapper extends EntityMapper<MenuItemDTO, MenuItemEntity> {
    @Mapping(target = "party", source = "party", qualifiedByName = "id")
    @Mapping(target = "food", source = "food", qualifiedByName = "id")
    MenuItemDTO toDto(MenuItemEntity s);
}
