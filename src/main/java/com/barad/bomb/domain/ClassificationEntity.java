package com.barad.bomb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ClassificationEntity.
 */
@Entity
@Table(name = "classification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ClassificationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @NotNull
    @Column(name = "class_code", nullable = false, unique = true)
    private Integer classCode;

    @Size(max = 300)
    @Column(name = "description", length = 300)
    private String description;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "classifications" }, allowSetters = true)
    private ClassTypeEntity classType;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClassificationEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public ClassificationEntity title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getClassCode() {
        return this.classCode;
    }

    public ClassificationEntity classCode(Integer classCode) {
        this.classCode = classCode;
        return this;
    }

    public void setClassCode(Integer classCode) {
        this.classCode = classCode;
    }

    public String getDescription() {
        return this.description;
    }

    public ClassificationEntity description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ClassTypeEntity getClassType() {
        return this.classType;
    }

    public ClassificationEntity classType(ClassTypeEntity classType) {
        this.setClassType(classType);
        return this;
    }

    public void setClassType(ClassTypeEntity classType) {
        this.classType = classType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassificationEntity)) {
            return false;
        }
        return id != null && id.equals(((ClassificationEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassificationEntity{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", classCode=" + getClassCode() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
