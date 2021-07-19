package com.barad.bomb.service.mapper;

import com.barad.bomb.domain.*;
import com.barad.bomb.service.dto.ClassificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClassificationEntity} and its DTO {@link ClassificationDTO}.
 */
@Mapper(componentModel = "spring", uses = { ClassTypeMapper.class })
public interface ClassificationMapper extends EntityMapper<ClassificationDTO, ClassificationEntity> {
    @Mapping(target = "classType", source = "classType", qualifiedByName = "id")
    ClassificationDTO toDto(ClassificationEntity s);
}
