package com.barad.bomb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.barad.bomb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FactorStatusHistoryEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FactorStatusHistoryEntity.class);
        FactorStatusHistoryEntity factorStatusHistoryEntity1 = new FactorStatusHistoryEntity();
        factorStatusHistoryEntity1.setId(1L);
        FactorStatusHistoryEntity factorStatusHistoryEntity2 = new FactorStatusHistoryEntity();
        factorStatusHistoryEntity2.setId(factorStatusHistoryEntity1.getId());
        assertThat(factorStatusHistoryEntity1).isEqualTo(factorStatusHistoryEntity2);
        factorStatusHistoryEntity2.setId(2L);
        assertThat(factorStatusHistoryEntity1).isNotEqualTo(factorStatusHistoryEntity2);
        factorStatusHistoryEntity1.setId(null);
        assertThat(factorStatusHistoryEntity1).isNotEqualTo(factorStatusHistoryEntity2);
    }
}
