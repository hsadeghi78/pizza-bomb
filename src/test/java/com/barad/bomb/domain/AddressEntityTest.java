package com.barad.bomb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.barad.bomb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AddressEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AddressEntity.class);
        AddressEntity addressEntity1 = new AddressEntity();
        addressEntity1.setId(1L);
        AddressEntity addressEntity2 = new AddressEntity();
        addressEntity2.setId(addressEntity1.getId());
        assertThat(addressEntity1).isEqualTo(addressEntity2);
        addressEntity2.setId(2L);
        assertThat(addressEntity1).isNotEqualTo(addressEntity2);
        addressEntity1.setId(null);
        assertThat(addressEntity1).isNotEqualTo(addressEntity2);
    }
}
