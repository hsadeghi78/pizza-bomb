package com.barad.bomb.service.mapper;

import com.barad.bomb.domain.*;
import com.barad.bomb.service.dto.FactorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FactorEntity} and its DTO {@link FactorDTO}.
 */
@Mapper(componentModel = "spring", uses = { PartyMapper.class, AddressMapper.class })
public interface FactorMapper extends EntityMapper<FactorDTO, FactorEntity> {
    @Mapping(target = "buyerParty", source = "buyerParty", qualifiedByName = "id")
    @Mapping(target = "sellerParty", source = "sellerParty", qualifiedByName = "id")
    @Mapping(target = "deliveryAddress", source = "deliveryAddress", qualifiedByName = "id")
    FactorDTO toDto(FactorEntity s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FactorDTO toDtoId(FactorEntity factorEntity);
}
