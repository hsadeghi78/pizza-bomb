package com.barad.bomb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.barad.bomb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MenuItemEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MenuItemEntity.class);
        MenuItemEntity menuItemEntity1 = new MenuItemEntity();
        menuItemEntity1.setId(1L);
        MenuItemEntity menuItemEntity2 = new MenuItemEntity();
        menuItemEntity2.setId(menuItemEntity1.getId());
        assertThat(menuItemEntity1).isEqualTo(menuItemEntity2);
        menuItemEntity2.setId(2L);
        assertThat(menuItemEntity1).isNotEqualTo(menuItemEntity2);
        menuItemEntity1.setId(null);
        assertThat(menuItemEntity1).isNotEqualTo(menuItemEntity2);
    }
}
