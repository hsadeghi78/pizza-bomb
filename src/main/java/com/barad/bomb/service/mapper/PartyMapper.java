package com.barad.bomb.service.mapper;

import com.barad.bomb.domain.*;
import com.barad.bomb.service.dto.PartyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PartyEntity} and its DTO {@link PartyDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PartyMapper extends EntityMapper<PartyDTO, PartyEntity> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PartyDTO toDtoId(PartyEntity partyEntity);
}
