package com.barad.bomb.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClassificationMapperTest {

    private ClassificationMapper classificationMapper;

    @BeforeEach
    public void setUp() {
        classificationMapper = new ClassificationMapperImpl();
    }
}
