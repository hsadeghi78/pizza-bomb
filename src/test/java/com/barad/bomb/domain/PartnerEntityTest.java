package com.barad.bomb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.barad.bomb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PartnerEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartnerEntity.class);
        PartnerEntity partnerEntity1 = new PartnerEntity();
        partnerEntity1.setId(1L);
        PartnerEntity partnerEntity2 = new PartnerEntity();
        partnerEntity2.setId(partnerEntity1.getId());
        assertThat(partnerEntity1).isEqualTo(partnerEntity2);
        partnerEntity2.setId(2L);
        assertThat(partnerEntity1).isNotEqualTo(partnerEntity2);
        partnerEntity1.setId(null);
        assertThat(partnerEntity1).isNotEqualTo(partnerEntity2);
    }
}
