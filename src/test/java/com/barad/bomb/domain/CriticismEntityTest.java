package com.barad.bomb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.barad.bomb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CriticismEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CriticismEntity.class);
        CriticismEntity criticismEntity1 = new CriticismEntity();
        criticismEntity1.setId(1L);
        CriticismEntity criticismEntity2 = new CriticismEntity();
        criticismEntity2.setId(criticismEntity1.getId());
        assertThat(criticismEntity1).isEqualTo(criticismEntity2);
        criticismEntity2.setId(2L);
        assertThat(criticismEntity1).isNotEqualTo(criticismEntity2);
        criticismEntity1.setId(null);
        assertThat(criticismEntity1).isNotEqualTo(criticismEntity2);
    }
}
