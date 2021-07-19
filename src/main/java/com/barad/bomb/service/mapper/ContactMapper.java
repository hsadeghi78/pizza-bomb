package com.barad.bomb.service.mapper;

import com.barad.bomb.domain.*;
import com.barad.bomb.service.dto.ContactDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContactEntity} and its DTO {@link ContactDTO}.
 */
@Mapper(componentModel = "spring", uses = { PartyMapper.class })
public interface ContactMapper extends EntityMapper<ContactDTO, ContactEntity> {
    @Mapping(target = "party", source = "party", qualifiedByName = "id")
    ContactDTO toDto(ContactEntity s);
}
