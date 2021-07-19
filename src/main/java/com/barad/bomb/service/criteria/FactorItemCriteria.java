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
 * Criteria class for the {@link com.barad.bomb.domain.FactorItemEntity} entity. This class is used
 * in {@link com.barad.bomb.web.rest.FactorItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /factor-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FactorItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter rowNum;

    private StringFilter title;

    private IntegerFilter count;

    private DoubleFilter discount;

    private DoubleFilter tax;

    private StringFilter description;

    private LongFilter foodId;

    private LongFilter factorId;

    public FactorItemCriteria() {}

    public FactorItemCriteria(FactorItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.rowNum = other.rowNum == null ? null : other.rowNum.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.count = other.count == null ? null : other.count.copy();
        this.discount = other.discount == null ? null : other.discount.copy();
        this.tax = other.tax == null ? null : other.tax.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.foodId = other.foodId == null ? null : other.foodId.copy();
        this.factorId = other.factorId == null ? null : other.factorId.copy();
    }

    @Override
    public FactorItemCriteria copy() {
        return new FactorItemCriteria(this);
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

    public IntegerFilter getRowNum() {
        return rowNum;
    }

    public IntegerFilter rowNum() {
        if (rowNum == null) {
            rowNum = new IntegerFilter();
        }
        return rowNum;
    }

    public void setRowNum(IntegerFilter rowNum) {
        this.rowNum = rowNum;
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

    public IntegerFilter getCount() {
        return count;
    }

    public IntegerFilter count() {
        if (count == null) {
            count = new IntegerFilter();
        }
        return count;
    }

    public void setCount(IntegerFilter count) {
        this.count = count;
    }

    public DoubleFilter getDiscount() {
        return discount;
    }

    public DoubleFilter discount() {
        if (discount == null) {
            discount = new DoubleFilter();
        }
        return discount;
    }

    public void setDiscount(DoubleFilter discount) {
        this.discount = discount;
    }

    public DoubleFilter getTax() {
        return tax;
    }

    public DoubleFilter tax() {
        if (tax == null) {
            tax = new DoubleFilter();
        }
        return tax;
    }

    public void setTax(DoubleFilter tax) {
        this.tax = tax;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FactorItemCriteria that = (FactorItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(rowNum, that.rowNum) &&
            Objects.equals(title, that.title) &&
            Objects.equals(count, that.count) &&
            Objects.equals(discount, that.discount) &&
            Objects.equals(tax, that.tax) &&
            Objects.equals(description, that.description) &&
            Objects.equals(foodId, that.foodId) &&
            Objects.equals(factorId, that.factorId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rowNum, title, count, discount, tax, description, foodId, factorId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactorItemCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (rowNum != null ? "rowNum=" + rowNum + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (count != null ? "count=" + count + ", " : "") +
            (discount != null ? "discount=" + discount + ", " : "") +
            (tax != null ? "tax=" + tax + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (foodId != null ? "foodId=" + foodId + ", " : "") +
            (factorId != null ? "factorId=" + factorId + ", " : "") +
            "}";
    }
}
