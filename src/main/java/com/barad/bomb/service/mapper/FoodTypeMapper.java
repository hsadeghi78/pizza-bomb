package com.barad.bomb.service.mapper;

import com.barad.bomb.domain.*;
import com.barad.bomb.service.dto.FoodTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FoodTypeEntity} and its DTO {@link FoodTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = { PartyMapper.class })
public interface FoodTypeMapper extends EntityMapper<FoodTypeDTO, FoodTypeEntity> {
    @Mapping(target = "party", source = "party", qualifiedByName = "id")
    FoodTypeDTO toDto(FoodTypeEntity s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FoodTypeDTO toDtoId(FoodTypeEntity foodTypeEntity);
}
