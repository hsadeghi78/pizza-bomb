package com.barad.bomb.service.mapper;

import com.barad.bomb.domain.*;
import com.barad.bomb.service.dto.BranchDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BranchEntity} and its DTO {@link BranchDTO}.
 */
@Mapper(componentModel = "spring", uses = { PartyMapper.class })
public interface BranchMapper extends EntityMapper<BranchDTO, BranchEntity> {
    @Mapping(target = "party", source = "party", qualifiedByName = "id")
    BranchDTO toDto(BranchEntity s);
}
