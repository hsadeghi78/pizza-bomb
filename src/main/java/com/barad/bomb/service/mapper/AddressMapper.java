package com.barad.bomb.service.mapper;

import com.barad.bomb.domain.*;
import com.barad.bomb.service.dto.AddressDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AddressEntity} and its DTO {@link AddressDTO}.
 */
@Mapper(componentModel = "spring", uses = { PartyMapper.class })
public interface AddressMapper extends EntityMapper<AddressDTO, AddressEntity> {
    @Mapping(target = "party", source = "party", qualifiedByName = "id")
    AddressDTO toDto(AddressEntity s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AddressDTO toDtoId(AddressEntity addressEntity);
}
