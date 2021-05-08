package com.barad.bomb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.barad.bomb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BranchEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BranchEntity.class);
        BranchEntity branchEntity1 = new BranchEntity();
        branchEntity1.setId(1L);
        BranchEntity branchEntity2 = new BranchEntity();
        branchEntity2.setId(branchEntity1.getId());
        assertThat(branchEntity1).isEqualTo(branchEntity2);
        branchEntity2.setId(2L);
        assertThat(branchEntity1).isNotEqualTo(branchEntity2);
        branchEntity1.setId(null);
        assertThat(branchEntity1).isNotEqualTo(branchEntity2);
    }
}
