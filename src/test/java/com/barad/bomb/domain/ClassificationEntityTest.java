package com.barad.bomb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.barad.bomb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassificationEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassificationEntity.class);
        ClassificationEntity classificationEntity1 = new ClassificationEntity();
        classificationEntity1.setId(1L);
        ClassificationEntity classificationEntity2 = new ClassificationEntity();
        classificationEntity2.setId(classificationEntity1.getId());
        assertThat(classificationEntity1).isEqualTo(classificationEntity2);
        classificationEntity2.setId(2L);
        assertThat(classificationEntity1).isNotEqualTo(classificationEntity2);
        classificationEntity1.setId(null);
        assertThat(classificationEntity1).isNotEqualTo(classificationEntity2);
    }
}
