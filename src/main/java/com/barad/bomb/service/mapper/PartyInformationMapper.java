package com.barad.bomb.service.mapper;

import com.barad.bomb.domain.*;
import com.barad.bomb.service.dto.PartyInformationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PartyInformationEntity} and its DTO {@link PartyInformationDTO}.
 */
@Mapper(componentModel = "spring", uses = { PartyMapper.class })
public interface PartyInformationMapper extends EntityMapper<PartyInformationDTO, PartyInformationEntity> {
    @Mapping(target = "party", source = "party", qualifiedByName = "id")
    PartyInformationDTO toDto(PartyInformationEntity s);
}
