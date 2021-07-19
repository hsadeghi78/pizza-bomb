package com.barad.bomb.service.mapper;

import com.barad.bomb.domain.*;
import com.barad.bomb.service.dto.CommentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CommentEntity} and its DTO {@link CommentDTO}.
 */
@Mapper(componentModel = "spring", uses = { PartyMapper.class })
public interface CommentMapper extends EntityMapper<CommentDTO, CommentEntity> {
    @Mapping(target = "writerParty", source = "writerParty", qualifiedByName = "id")
    @Mapping(target = "audienceParty", source = "audienceParty", qualifiedByName = "id")
    @Mapping(target = "parent", source = "parent", qualifiedByName = "id")
    CommentDTO toDto(CommentEntity s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CommentDTO toDtoId(CommentEntity commentEntity);
}
