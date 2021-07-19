package com.barad.bomb.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PartnerMapperTest {

    private PartnerMapper partnerMapper;

    @BeforeEach
    public void setUp() {
        partnerMapper = new PartnerMapperImpl();
    }
}
