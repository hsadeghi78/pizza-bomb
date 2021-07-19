package com.barad.bomb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.barad.bomb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FactorItemEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FactorItemEntity.class);
        FactorItemEntity factorItemEntity1 = new FactorItemEntity();
        factorItemEntity1.setId(1L);
        FactorItemEntity factorItemEntity2 = new FactorItemEntity();
        factorItemEntity2.setId(factorItemEntity1.getId());
        assertThat(factorItemEntity1).isEqualTo(factorItemEntity2);
        factorItemEntity2.setId(2L);
        assertThat(factorItemEntity1).isNotEqualTo(factorItemEntity2);
        factorItemEntity1.setId(null);
        assertThat(factorItemEntity1).isNotEqualTo(factorItemEntity2);
    }
}
