package com.barad.bomb.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FactorMapperTest {

    private FactorMapper factorMapper;

    @BeforeEach
    public void setUp() {
        factorMapper = new FactorMapperImpl();
    }
}
