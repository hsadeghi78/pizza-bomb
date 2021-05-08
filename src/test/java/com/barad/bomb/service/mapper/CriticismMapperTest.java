package com.barad.bomb.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CriticismMapperTest {

    private CriticismMapper criticismMapper;

    @BeforeEach
    public void setUp() {
        criticismMapper = new CriticismMapperImpl();
    }
}
