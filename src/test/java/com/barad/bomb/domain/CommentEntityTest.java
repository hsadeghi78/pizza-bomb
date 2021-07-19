package com.barad.bomb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.barad.bomb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommentEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommentEntity.class);
        CommentEntity commentEntity1 = new CommentEntity();
        commentEntity1.setId(1L);
        CommentEntity commentEntity2 = new CommentEntity();
        commentEntity2.setId(commentEntity1.getId());
        assertThat(commentEntity1).isEqualTo(commentEntity2);
        commentEntity2.setId(2L);
        assertThat(commentEntity1).isNotEqualTo(commentEntity2);
        commentEntity1.setId(null);
        assertThat(commentEntity1).isNotEqualTo(commentEntity2);
    }
}
