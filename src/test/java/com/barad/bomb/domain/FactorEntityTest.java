package com.barad.bomb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.barad.bomb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FactorEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FactorEntity.class);
        FactorEntity factorEntity1 = new FactorEntity();
        factorEntity1.setId(1L);
        FactorEntity factorEntity2 = new FactorEntity();
        factorEntity2.setId(factorEntity1.getId());
        assertThat(factorEntity1).isEqualTo(factorEntity2);
        factorEntity2.setId(2L);
        assertThat(factorEntity1).isNotEqualTo(factorEntity2);
        factorEntity1.setId(null);
        assertThat(factorEntity1).isNotEqualTo(factorEntity2);
    }
}
