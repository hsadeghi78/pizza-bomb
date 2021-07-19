package com.barad.bomb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.barad.bomb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FoodTypeEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FoodTypeEntity.class);
        FoodTypeEntity foodTypeEntity1 = new FoodTypeEntity();
        foodTypeEntity1.setId(1L);
        FoodTypeEntity foodTypeEntity2 = new FoodTypeEntity();
        foodTypeEntity2.setId(foodTypeEntity1.getId());
        assertThat(foodTypeEntity1).isEqualTo(foodTypeEntity2);
        foodTypeEntity2.setId(2L);
        assertThat(foodTypeEntity1).isNotEqualTo(foodTypeEntity2);
        foodTypeEntity1.setId(null);
        assertThat(foodTypeEntity1).isNotEqualTo(foodTypeEntity2);
    }
}
