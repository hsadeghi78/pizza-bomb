package com.barad.bomb.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClassTypeMapperTest {

    private ClassTypeMapper classTypeMapper;

    @BeforeEach
    public void setUp() {
        classTypeMapper = new ClassTypeMapperImpl();
    }
}
