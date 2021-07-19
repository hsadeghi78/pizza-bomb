package com.barad.bomb.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.barad.bomb.domain.ClassificationEntity} entity.
 */
public class ClassificationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String title;

    @NotNull
    private Integer classCode;

    @Size(max = 300)
    private String description;

    private ClassTypeDTO classType;

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

    public Integer getClassCode() {
        return classCode;
    }

    public void setClassCode(Integer classCode) {
        this.classCode = classCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ClassTypeDTO getClassType() {
        return classType;
    }

    public void setClassType(ClassTypeDTO classType) {
        this.classType = classType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassificationDTO)) {
            return false;
        }

        ClassificationDTO classificationDTO = (ClassificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, classificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassificationDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", classCode=" + getClassCode() +
            ", description='" + getDescription() + "'" +
            ", classType=" + getClassType() +
            "}";
    }
}
