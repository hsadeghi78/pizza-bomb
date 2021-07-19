package com.barad.bomb.service.mapper;

import com.barad.bomb.domain.*;
import com.barad.bomb.service.dto.ConsumeMaterialDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConsumeMaterialEntity} and its DTO {@link ConsumeMaterialDTO}.
 */
@Mapper(componentModel = "spring", uses = { FoodMapper.class })
public interface ConsumeMaterialMapper extends EntityMapper<ConsumeMaterialDTO, ConsumeMaterialEntity> {
    @Mapping(target = "food", source = "food", qualifiedByName = "id")
    ConsumeMaterialDTO toDto(ConsumeMaterialEntity s);
}
