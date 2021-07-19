package com.barad.bomb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.barad.bomb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassTypeEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassTypeEntity.class);
        ClassTypeEntity classTypeEntity1 = new ClassTypeEntity();
        classTypeEntity1.setId(1L);
        ClassTypeEntity classTypeEntity2 = new ClassTypeEntity();
        classTypeEntity2.setId(classTypeEntity1.getId());
        assertThat(classTypeEntity1).isEqualTo(classTypeEntity2);
        classTypeEntity2.setId(2L);
        assertThat(classTypeEntity1).isNotEqualTo(classTypeEntity2);
        classTypeEntity1.setId(null);
        assertThat(classTypeEntity1).isNotEqualTo(classTypeEntity2);
    }
}
