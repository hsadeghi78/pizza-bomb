package com.barad.bomb.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PartyMapperTest {

    private PartyMapper partyMapper;

    @BeforeEach
    public void setUp() {
        partyMapper = new PartyMapperImpl();
    }
}
