package com.barad.bomb.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.barad.bomb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FactorStatusHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FactorStatusHistoryDTO.class);
        FactorStatusHistoryDTO factorStatusHistoryDTO1 = new FactorStatusHistoryDTO();
        factorStatusHistoryDTO1.setId(1L);
        FactorStatusHistoryDTO factorStatusHistoryDTO2 = new FactorStatusHistoryDTO();
        assertThat(factorStatusHistoryDTO1).isNotEqualTo(factorStatusHistoryDTO2);
        factorStatusHistoryDTO2.setId(factorStatusHistoryDTO1.getId());
        assertThat(factorStatusHistoryDTO1).isEqualTo(factorStatusHistoryDTO2);
        factorStatusHistoryDTO2.setId(2L);
        assertThat(factorStatusHistoryDTO1).isNotEqualTo(factorStatusHistoryDTO2);
        factorStatusHistoryDTO1.setId(null);
        assertThat(factorStatusHistoryDTO1).isNotEqualTo(factorStatusHistoryDTO2);
    }
}
