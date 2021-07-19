package com.barad.bomb.service.mapper;

import com.barad.bomb.domain.*;
import com.barad.bomb.service.dto.ClassTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClassTypeEntity} and its DTO {@link ClassTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClassTypeMapper extends EntityMapper<ClassTypeDTO, ClassTypeEntity> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClassTypeDTO toDtoId(ClassTypeEntity classTypeEntity);
}
