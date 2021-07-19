package com.barad.bomb.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FactorStatusHistoryMapperTest {

    private FactorStatusHistoryMapper factorStatusHistoryMapper;

    @BeforeEach
    public void setUp() {
        factorStatusHistoryMapper = new FactorStatusHistoryMapperImpl();
    }
}
