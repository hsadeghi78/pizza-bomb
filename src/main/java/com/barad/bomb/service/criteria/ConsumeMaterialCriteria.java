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
 * Criteria class for the {@link com.barad.bomb.domain.ConsumeMaterialEntity} entity. This class is used
 * in {@link com.barad.bomb.web.rest.ConsumeMaterialResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /consume-materials?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ConsumeMaterialCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter type;

    private DoubleFilter amount;

    private LongFilter amountUnitClassId;

    private LongFilter foodId;

    public ConsumeMaterialCriteria() {}

    public ConsumeMaterialCriteria(ConsumeMaterialCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.amountUnitClassId = other.amountUnitClassId == null ? null : other.amountUnitClassId.copy();
        this.foodId = other.foodId == null ? null : other.foodId.copy();
    }

    @Override
    public ConsumeMaterialCriteria copy() {
        return new ConsumeMaterialCriteria(this);
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

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getType() {
        return type;
    }

    public StringFilter type() {
        if (type == null) {
            type = new StringFilter();
        }
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public DoubleFilter getAmount() {
        return amount;
    }

    public DoubleFilter amount() {
        if (amount == null) {
            amount = new DoubleFilter();
        }
        return amount;
    }

    public void setAmount(DoubleFilter amount) {
        this.amount = amount;
    }

    public LongFilter getAmountUnitClassId() {
        return amountUnitClassId;
    }

    public LongFilter amountUnitClassId() {
        if (amountUnitClassId == null) {
            amountUnitClassId = new LongFilter();
        }
        return amountUnitClassId;
    }

    public void setAmountUnitClassId(LongFilter amountUnitClassId) {
        this.amountUnitClassId = amountUnitClassId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ConsumeMaterialCriteria that = (ConsumeMaterialCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(type, that.type) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(amountUnitClassId, that.amountUnitClassId) &&
            Objects.equals(foodId, that.foodId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, type, amount, amountUnitClassId, foodId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConsumeMaterialCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (amount != null ? "amount=" + amount + ", " : "") +
            (amountUnitClassId != null ? "amountUnitClassId=" + amountUnitClassId + ", " : "") +
            (foodId != null ? "foodId=" + foodId + ", " : "") +
            "}";
    }
}
