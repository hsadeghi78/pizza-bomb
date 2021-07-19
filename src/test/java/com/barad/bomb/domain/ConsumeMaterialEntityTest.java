package com.barad.bomb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.barad.bomb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConsumeMaterialEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConsumeMaterialEntity.class);
        ConsumeMaterialEntity consumeMaterialEntity1 = new ConsumeMaterialEntity();
        consumeMaterialEntity1.setId(1L);
        ConsumeMaterialEntity consumeMaterialEntity2 = new ConsumeMaterialEntity();
        consumeMaterialEntity2.setId(consumeMaterialEntity1.getId());
        assertThat(consumeMaterialEntity1).isEqualTo(consumeMaterialEntity2);
        consumeMaterialEntity2.setId(2L);
        assertThat(consumeMaterialEntity1).isNotEqualTo(consumeMaterialEntity2);
        consumeMaterialEntity1.setId(null);
        assertThat(consumeMaterialEntity1).isNotEqualTo(consumeMaterialEntity2);
    }
}
