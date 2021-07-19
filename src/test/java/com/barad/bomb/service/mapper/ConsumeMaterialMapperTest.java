package com.barad.bomb.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConsumeMaterialMapperTest {

    private ConsumeMaterialMapper consumeMaterialMapper;

    @BeforeEach
    public void setUp() {
        consumeMaterialMapper = new ConsumeMaterialMapperImpl();
    }
}
