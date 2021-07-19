package com.barad.bomb.service.mapper;

import com.barad.bomb.domain.*;
import com.barad.bomb.service.dto.FoodDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FoodEntity} and its DTO {@link FoodDTO}.
 */
@Mapper(componentModel = "spring", uses = { PartyMapper.class, FoodTypeMapper.class })
public interface FoodMapper extends EntityMapper<FoodDTO, FoodEntity> {
    @Mapping(target = "producerParty", source = "producerParty", qualifiedByName = "id")
    @Mapping(target = "designerParty", source = "designerParty", qualifiedByName = "id")
    @Mapping(target = "foodType", source = "foodType", qualifiedByName = "id")
    FoodDTO toDto(FoodEntity s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FoodDTO toDtoId(FoodEntity foodEntity);
}
