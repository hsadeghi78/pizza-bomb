package com.barad.bomb.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.barad.bomb.domain.ClassTypeEntity} entity.
 */
public class ClassTypeDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String title;

    @NotNull
    private Integer typeCode;

    @Size(max = 300)
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(Integer typeCode) {
        this.typeCode = typeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassTypeDTO)) {
            return false;
        }

        ClassTypeDTO classTypeDTO = (ClassTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, classTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassTypeDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", typeCode=" + getTypeCode() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
