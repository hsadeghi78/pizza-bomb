package com.barad.bomb.service.mapper;

import com.barad.bomb.domain.*;
import com.barad.bomb.service.dto.FactorStatusHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FactorStatusHistoryEntity} and its DTO {@link FactorStatusHistoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FactorStatusHistoryMapper extends EntityMapper<FactorStatusHistoryDTO, FactorStatusHistoryEntity> {}
