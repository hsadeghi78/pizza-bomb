package com.barad.bomb.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileDocumentMapperTest {

    private FileDocumentMapper fileDocumentMapper;

    @BeforeEach
    public void setUp() {
        fileDocumentMapper = new FileDocumentMapperImpl();
    }
}
