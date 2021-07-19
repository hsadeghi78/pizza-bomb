package com.barad.bomb.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FactorItemMapperTest {

    private FactorItemMapper factorItemMapper;

    @BeforeEach
    public void setUp() {
        factorItemMapper = new FactorItemMapperImpl();
    }
}
