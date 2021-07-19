package com.barad.bomb.service.mapper;

import com.barad.bomb.domain.*;
import com.barad.bomb.service.dto.PriceHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PriceHistoryEntity} and its DTO {@link PriceHistoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PriceHistoryMapper extends EntityMapper<PriceHistoryDTO, PriceHistoryEntity> {}
