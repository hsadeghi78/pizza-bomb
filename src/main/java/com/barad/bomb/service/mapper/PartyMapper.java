package com.barad.bomb.service.mapper;

import com.barad.bomb.domain.*;
import com.barad.bomb.service.dto.PartyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PartyEntity} and its DTO {@link PartyDTO}.
 */
@Mapper(componentModel = "spring", uses = { PartnerMapper.class, PersonMapper.class })
public interface PartyMapper extends EntityMapper<PartyDTO, PartyEntity> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "id")
    @Mapping(target = "partner", source = "partner", qualifiedByName = "id")
    @Mapping(target = "person", source = "person", qualifiedByName = "id")
    PartyDTO toDto(PartyEntity s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PartyDTO toDtoId(PartyEntity partyEntity);
}
