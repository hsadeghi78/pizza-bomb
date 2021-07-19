package com.barad.bomb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.barad.bomb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FileDocumentEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileDocumentEntity.class);
        FileDocumentEntity fileDocumentEntity1 = new FileDocumentEntity();
        fileDocumentEntity1.setId(1L);
        FileDocumentEntity fileDocumentEntity2 = new FileDocumentEntity();
        fileDocumentEntity2.setId(fileDocumentEntity1.getId());
        assertThat(fileDocumentEntity1).isEqualTo(fileDocumentEntity2);
        fileDocumentEntity2.setId(2L);
        assertThat(fileDocumentEntity1).isNotEqualTo(fileDocumentEntity2);
        fileDocumentEntity1.setId(null);
        assertThat(fileDocumentEntity1).isNotEqualTo(fileDocumentEntity2);
    }
}
