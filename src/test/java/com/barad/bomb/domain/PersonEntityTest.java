package com.barad.bomb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.barad.bomb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonEntity.class);
        PersonEntity personEntity1 = new PersonEntity();
        personEntity1.setId(1L);
        PersonEntity personEntity2 = new PersonEntity();
        personEntity2.setId(personEntity1.getId());
        assertThat(personEntity1).isEqualTo(personEntity2);
        personEntity2.setId(2L);
        assertThat(personEntity1).isNotEqualTo(personEntity2);
        personEntity1.setId(null);
        assertThat(personEntity1).isNotEqualTo(personEntity2);
    }
}
