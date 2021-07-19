package com.barad.bomb.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.barad.bomb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FoodTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FoodTypeDTO.class);
        FoodTypeDTO foodTypeDTO1 = new FoodTypeDTO();
        foodTypeDTO1.setId(1L);
        FoodTypeDTO foodTypeDTO2 = new FoodTypeDTO();
        assertThat(foodTypeDTO1).isNotEqualTo(foodTypeDTO2);
        foodTypeDTO2.setId(foodTypeDTO1.getId());
        assertThat(foodTypeDTO1).isEqualTo(foodTypeDTO2);
        foodTypeDTO2.setId(2L);
        assertThat(foodTypeDTO1).isNotEqualTo(foodTypeDTO2);
        foodTypeDTO1.setId(null);
        assertThat(foodTypeDTO1).isNotEqualTo(foodTypeDTO2);
    }
}
