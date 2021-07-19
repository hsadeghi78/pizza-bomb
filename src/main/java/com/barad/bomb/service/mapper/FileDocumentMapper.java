package com.barad.bomb.service.mapper;

import com.barad.bomb.domain.*;
import com.barad.bomb.service.dto.FileDocumentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FileDocumentEntity} and its DTO {@link FileDocumentDTO}.
 */
@Mapper(componentModel = "spring", uses = { PartyMapper.class })
public interface FileDocumentMapper extends EntityMapper<FileDocumentDTO, FileDocumentEntity> {
    @Mapping(target = "party", source = "party", qualifiedByName = "id")
    FileDocumentDTO toDto(FileDocumentEntity s);
}
