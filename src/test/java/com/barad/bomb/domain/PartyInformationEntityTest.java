package com.barad.bomb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.barad.bomb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PartyInformationEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartyInformationEntity.class);
        PartyInformationEntity partyInformationEntity1 = new PartyInformationEntity();
        partyInformationEntity1.setId(1L);
        PartyInformationEntity partyInformationEntity2 = new PartyInformationEntity();
        partyInformationEntity2.setId(partyInformationEntity1.getId());
        assertThat(partyInformationEntity1).isEqualTo(partyInformationEntity2);
        partyInformationEntity2.setId(2L);
        assertThat(partyInformationEntity1).isNotEqualTo(partyInformationEntity2);
        partyInformationEntity1.setId(null);
        assertThat(partyInformationEntity1).isNotEqualTo(partyInformationEntity2);
    }
}
