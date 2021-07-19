package com.barad.bomb.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FoodTypeMapperTest {

    private FoodTypeMapper foodTypeMapper;

    @BeforeEach
    public void setUp() {
        foodTypeMapper = new FoodTypeMapperImpl();
    }
}
