package com.barad.bomb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ClassTypeEntity.
 */
@Entity
@Table(name = "class_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ClassTypeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @NotNull
    @Column(name = "type_code", nullable = false, unique = true)
    private Integer typeCode;

    @Size(max = 300)
    @Column(name = "description", length = 300)
    private String description;

    @OneToMany(mappedBy = "classType")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "classType" }, allowSetters = true)
    private Set<ClassificationEntity> classifications = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClassTypeEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public ClassTypeEntity title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTypeCode() {
        return this.typeCode;
    }

    public ClassTypeEntity typeCode(Integer typeCode) {
        this.typeCode = typeCode;
        return this;
    }

    public void setTypeCode(Integer typeCode) {
        this.typeCode = typeCode;
    }

    public String getDescription() {
        return this.description;
    }

    public ClassTypeEntity description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ClassificationEntity> getClassifications() {
        return this.classifications;
    }

    public ClassTypeEntity classifications(Set<ClassificationEntity> classifications) {
        this.setClassifications(classifications);
        return this;
    }

    public ClassTypeEntity addClassifications(ClassificationEntity classification) {
        this.classifications.add(classification);
        classification.setClassType(this);
        return this;
    }

    public ClassTypeEntity removeClassifications(ClassificationEntity classification) {
        this.classifications.remove(classification);
        classification.setClassType(null);
        return this;
    }

    public void setClassifications(Set<ClassificationEntity> classifications) {
        if (this.classifications != null) {
            this.classifications.forEach(i -> i.setClassType(null));
        }
        if (classifications != null) {
            classifications.forEach(i -> i.setClassType(this));
        }
        this.classifications = classifications;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassTypeEntity)) {
            return false;
        }
        return id != null && id.equals(((ClassTypeEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassTypeEntity{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", typeCode=" + getTypeCode() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
