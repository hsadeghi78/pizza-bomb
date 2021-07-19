package com.barad.bomb.service.mapper;

import com.barad.bomb.domain.*;
import com.barad.bomb.service.dto.FactorItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FactorItemEntity} and its DTO {@link FactorItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { FoodMapper.class, FactorMapper.class })
public interface FactorItemMapper extends EntityMapper<FactorItemDTO, FactorItemEntity> {
    @Mapping(target = "food", source = "food", qualifiedByName = "id")
    @Mapping(target = "factor", source = "factor", qualifiedByName = "id")
    FactorItemDTO toDto(FactorItemEntity s);
}
