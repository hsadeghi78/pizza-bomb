package com.barad.bomb.service.mapper;

import com.barad.bomb.domain.*;
import com.barad.bomb.service.dto.PartnerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PartnerEntity} and its DTO {@link PartnerDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PartnerMapper extends EntityMapper<PartnerDTO, PartnerEntity> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PartnerDTO toDtoId(PartnerEntity partnerEntity);
}
