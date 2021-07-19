package com.barad.bomb.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PriceHistoryMapperTest {

    private PriceHistoryMapper priceHistoryMapper;

    @BeforeEach
    public void setUp() {
        priceHistoryMapper = new PriceHistoryMapperImpl();
    }
}
