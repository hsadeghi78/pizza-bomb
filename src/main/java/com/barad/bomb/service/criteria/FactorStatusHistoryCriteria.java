package com.barad.bomb.service.criteria;

import com.barad.bomb.domain.enumeration.FactorStatus;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.barad.bomb.domain.FactorStatusHistoryEntity} entity. This class is used
 * in {@link com.barad.bomb.web.rest.FactorStatusHistoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /factor-status-histories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FactorStatusHistoryCriteria implements Serializable, Criteria {

    /**
     * Class for filtering FactorStatus
     */
    public static class FactorStatusFilter extends Filter<FactorStatus> {

        public FactorStatusFilter() {}

        public FactorStatusFilter(FactorStatusFilter filter) {
            super(filter);
        }

        @Override
        public FactorStatusFilter copy() {
            return new FactorStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter factorId;

    private FactorStatusFilter status;

    public FactorStatusHistoryCriteria() {}

    public FactorStatusHistoryCriteria(FactorStatusHistoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.factorId = other.factorId == null ? null : other.factorId.copy();
        this.status = other.status == null ? null : other.status.copy();
    }

    @Override
    public FactorStatusHistoryCriteria copy() {
        return new FactorStatusHistoryCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getFactorId() {
        return factorId;
    }

    public LongFilter factorId() {
        if (factorId == null) {
            factorId = new LongFilter();
        }
        return factorId;
    }

    public void setFactorId(LongFilter factorId) {
        this.factorId = factorId;
    }

    public FactorStatusFilter getStatus() {
        return status;
    }

    public FactorStatusFilter status() {
        if (status == null) {
            status = new FactorStatusFilter();
        }
        return status;
    }

    public void setStatus(FactorStatusFilter status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FactorStatusHistoryCriteria that = (FactorStatusHistoryCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(factorId, that.factorId) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, factorId, status);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactorStatusHistoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (factorId != null ? "factorId=" + factorId + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            "}";
    }
}
