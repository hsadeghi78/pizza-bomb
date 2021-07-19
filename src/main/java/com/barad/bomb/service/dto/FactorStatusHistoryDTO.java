package com.barad.bomb.service.dto;

import com.barad.bomb.domain.enumeration.FactorStatus;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.barad.bomb.domain.FactorStatusHistoryEntity} entity.
 */
@ApiModel(description = "4 field fixed")
public class FactorStatusHistoryDTO implements Serializable {

    private Long id;

    @NotNull
    private Long factorId;

    @NotNull
    private FactorStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFactorId() {
        return factorId;
    }

    public void setFactorId(Long factorId) {
        this.factorId = factorId;
    }

    public FactorStatus getStatus() {
        return status;
    }

    public void setStatus(FactorStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FactorStatusHistoryDTO)) {
            return false;
        }

        FactorStatusHistoryDTO factorStatusHistoryDTO = (FactorStatusHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, factorStatusHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactorStatusHistoryDTO{" +
            "id=" + getId() +
            ", factorId=" + getFactorId() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
