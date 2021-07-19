package com.barad.bomb.service.criteria;

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
 * Criteria class for the {@link com.barad.bomb.domain.PriceHistoryEntity} entity. This class is used
 * in {@link com.barad.bomb.web.rest.PriceHistoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /price-histories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PriceHistoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter foodId;

    private LongFilter materialId;

    private DoubleFilter price;

    public PriceHistoryCriteria() {}

    public PriceHistoryCriteria(PriceHistoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.foodId = other.foodId == null ? null : other.foodId.copy();
        this.materialId = other.materialId == null ? null : other.materialId.copy();
        this.price = other.price == null ? null : other.price.copy();
    }

    @Override
    public PriceHistoryCriteria copy() {
        return new PriceHistoryCriteria(this);
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

    public LongFilter getFoodId() {
        return foodId;
    }

    public LongFilter foodId() {
        if (foodId == null) {
            foodId = new LongFilter();
        }
        return foodId;
    }

    public void setFoodId(LongFilter foodId) {
        this.foodId = foodId;
    }

    public LongFilter getMaterialId() {
        return materialId;
    }

    public LongFilter materialId() {
        if (materialId == null) {
            materialId = new LongFilter();
        }
        return materialId;
    }

    public void setMaterialId(LongFilter materialId) {
        this.materialId = materialId;
    }

    public DoubleFilter getPrice() {
        return price;
    }

    public DoubleFilter price() {
        if (price == null) {
            price = new DoubleFilter();
        }
        return price;
    }

    public void setPrice(DoubleFilter price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PriceHistoryCriteria that = (PriceHistoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(foodId, that.foodId) &&
            Objects.equals(materialId, that.materialId) &&
            Objects.equals(price, that.price)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, foodId, materialId, price);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PriceHistoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (foodId != null ? "foodId=" + foodId + ", " : "") +
            (materialId != null ? "materialId=" + materialId + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            "}";
    }
}
