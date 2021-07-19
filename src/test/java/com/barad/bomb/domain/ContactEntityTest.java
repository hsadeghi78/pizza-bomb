package com.barad.bomb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.barad.bomb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactEntity.class);
        ContactEntity contactEntity1 = new ContactEntity();
        contactEntity1.setId(1L);
        ContactEntity contactEntity2 = new ContactEntity();
        contactEntity2.setId(contactEntity1.getId());
        assertThat(contactEntity1).isEqualTo(contactEntity2);
        contactEntity2.setId(2L);
        assertThat(contactEntity1).isNotEqualTo(contactEntity2);
        contactEntity1.setId(null);
        assertThat(contactEntity1).isNotEqualTo(contactEntity2);
    }
}
