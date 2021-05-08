package com.barad.bomb.service.mapper;

import com.barad.bomb.domain.*;
import com.barad.bomb.service.dto.CriticismDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CriticismEntity} and its DTO {@link CriticismDTO}.
 */
@Mapper(componentModel = "spring", uses = { PartyMapper.class })
public interface CriticismMapper extends EntityMapper<CriticismDTO, CriticismEntity> {
    @Mapping(target = "party", source = "party", qualifiedByName = "id")
    CriticismDTO toDto(CriticismEntity s);
}
