package com.barad.bomb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.barad.bomb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PartyEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartyEntity.class);
        PartyEntity partyEntity1 = new PartyEntity();
        partyEntity1.setId(1L);
        PartyEntity partyEntity2 = new PartyEntity();
        partyEntity2.setId(partyEntity1.getId());
        assertThat(partyEntity1).isEqualTo(partyEntity2);
        partyEntity2.setId(2L);
        assertThat(partyEntity1).isNotEqualTo(partyEntity2);
        partyEntity1.setId(null);
        assertThat(partyEntity1).isNotEqualTo(partyEntity2);
    }
}
