package com.barad.bomb.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PartyInformationMapperTest {

    private PartyInformationMapper partyInformationMapper;

    @BeforeEach
    public void setUp() {
        partyInformationMapper = new PartyInformationMapperImpl();
    }
}
