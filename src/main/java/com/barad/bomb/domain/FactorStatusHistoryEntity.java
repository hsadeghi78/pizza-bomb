package com.barad.bomb.domain;

import com.barad.bomb.domain.enumeration.FactorStatus;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 4 field fixed
 */
@Entity
@Table(name = "factor_status_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FactorStatusHistoryEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "factor_id", nullable = false)
    private Long factorId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FactorStatus status;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FactorStatusHistoryEntity id(Long id) {
        this.id = id;
        return this;
    }

    public Long getFactorId() {
        return this.factorId;
    }

    public FactorStatusHistoryEntity factorId(Long factorId) {
        this.factorId = factorId;
        return this;
    }

    public void setFactorId(Long factorId) {
        this.factorId = factorId;
    }

    public FactorStatus getStatus() {
        return this.status;
    }

    public FactorStatusHistoryEntity status(FactorStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(FactorStatus status) {
        this.status = status;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FactorStatusHistoryEntity)) {
            return false;
        }
        return id != null && id.equals(((FactorStatusHistoryEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactorStatusHistoryEntity{" +
            "id=" + getId() +
            ", factorId=" + getFactorId() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
