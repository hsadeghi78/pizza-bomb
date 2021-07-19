package com.barad.bomb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.barad.bomb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PriceHistoryEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PriceHistoryEntity.class);
        PriceHistoryEntity priceHistoryEntity1 = new PriceHistoryEntity();
        priceHistoryEntity1.setId(1L);
        PriceHistoryEntity priceHistoryEntity2 = new PriceHistoryEntity();
        priceHistoryEntity2.setId(priceHistoryEntity1.getId());
        assertThat(priceHistoryEntity1).isEqualTo(priceHistoryEntity2);
        priceHistoryEntity2.setId(2L);
        assertThat(priceHistoryEntity1).isNotEqualTo(priceHistoryEntity2);
        priceHistoryEntity1.setId(null);
        assertThat(priceHistoryEntity1).isNotEqualTo(priceHistoryEntity2);
    }
}
