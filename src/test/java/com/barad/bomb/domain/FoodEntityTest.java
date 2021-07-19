package com.barad.bomb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.barad.bomb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FoodEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FoodEntity.class);
        FoodEntity foodEntity1 = new FoodEntity();
        foodEntity1.setId(1L);
        FoodEntity foodEntity2 = new FoodEntity();
        foodEntity2.setId(foodEntity1.getId());
        assertThat(foodEntity1).isEqualTo(foodEntity2);
        foodEntity2.setId(2L);
        assertThat(foodEntity1).isNotEqualTo(foodEntity2);
        foodEntity1.setId(null);
        assertThat(foodEntity1).isNotEqualTo(foodEntity2);
    }
}
