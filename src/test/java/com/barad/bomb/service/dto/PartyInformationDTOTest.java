package com.barad.bomb.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.barad.bomb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PartyInformationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartyInformationDTO.class);
        PartyInformationDTO partyInformationDTO1 = new PartyInformationDTO();
        partyInformationDTO1.setId(1L);
        PartyInformationDTO partyInformationDTO2 = new PartyInformationDTO();
        assertThat(partyInformationDTO1).isNotEqualTo(partyInformationDTO2);
        partyInformationDTO2.setId(partyInformationDTO1.getId());
        assertThat(partyInformationDTO1).isEqualTo(partyInformationDTO2);
        partyInformationDTO2.setId(2L);
        assertThat(partyInformationDTO1).isNotEqualTo(partyInformationDTO2);
        partyInformationDTO1.setId(null);
        assertThat(partyInformationDTO1).isNotEqualTo(partyInformationDTO2);
    }
}
