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
 * Criteria class for the {@link com.barad.bomb.domain.FoodEntity} entity. This class is used
 * in {@link com.barad.bomb.web.rest.FoodResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /foods?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FoodCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter foodCode;

    private LongFilter sizeClassId;

    private LongFilter categoryClassId;

    private DoubleFilter lastPrice;

    private StringFilter description;

    private LongFilter menuItemsId;

    private LongFilter factorItemsId;

    private LongFilter materialsId;

    private LongFilter producerPartyId;

    private LongFilter designerPartyId;

    private LongFilter foodTypeId;

    public FoodCriteria() {}

    public FoodCriteria(FoodCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.foodCode = other.foodCode == null ? null : other.foodCode.copy();
        this.sizeClassId = other.sizeClassId == null ? null : other.sizeClassId.copy();
        this.categoryClassId = other.categoryClassId == null ? null : other.categoryClassId.copy();
        this.lastPrice = other.lastPrice == null ? null : other.lastPrice.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.menuItemsId = other.menuItemsId == null ? null : other.menuItemsId.copy();
        this.factorItemsId = other.factorItemsId == null ? null : other.factorItemsId.copy();
        this.materialsId = other.materialsId == null ? null : other.materialsId.copy();
        this.producerPartyId = other.producerPartyId == null ? null : other.producerPartyId.copy();
        this.designerPartyId = other.designerPartyId == null ? null : other.designerPartyId.copy();
        this.foodTypeId = other.foodTypeId == null ? null : other.foodTypeId.copy();
    }

    @Override
    public FoodCriteria copy() {
        return new FoodCriteria(this);
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

    public StringFilter getFoodCode() {
        return foodCode;
    }

    public StringFilter foodCode() {
        if (foodCode == null) {
            foodCode = new StringFilter();
        }
        return foodCode;
    }

    public void setFoodCode(StringFilter foodCode) {
        this.foodCode = foodCode;
    }

    public LongFilter getSizeClassId() {
        return sizeClassId;
    }

    public LongFilter sizeClassId() {
        if (sizeClassId == null) {
            sizeClassId = new LongFilter();
        }
        return sizeClassId;
    }

    public void setSizeClassId(LongFilter sizeClassId) {
        this.sizeClassId = sizeClassId;
    }

    public LongFilter getCategoryClassId() {
        return categoryClassId;
    }

    public LongFilter categoryClassId() {
        if (categoryClassId == null) {
            categoryClassId = new LongFilter();
        }
        return categoryClassId;
    }

    public void setCategoryClassId(LongFilter categoryClassId) {
        this.categoryClassId = categoryClassId;
    }

    public DoubleFilter getLastPrice() {
        return lastPrice;
    }

    public DoubleFilter lastPrice() {
        if (lastPrice == null) {
            lastPrice = new DoubleFilter();
        }
        return lastPrice;
    }

    public void setLastPrice(DoubleFilter lastPrice) {
        this.lastPrice = lastPrice;
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

    public LongFilter getMenuItemsId() {
        return menuItemsId;
    }

    public LongFilter menuItemsId() {
        if (menuItemsId == null) {
            menuItemsId = new LongFilter();
        }
        return menuItemsId;
    }

    public void setMenuItemsId(LongFilter menuItemsId) {
        this.menuItemsId = menuItemsId;
    }

    public LongFilter getFactorItemsId() {
        return factorItemsId;
    }

    public LongFilter factorItemsId() {
        if (factorItemsId == null) {
            factorItemsId = new LongFilter();
        }
        return factorItemsId;
    }

    public void setFactorItemsId(LongFilter factorItemsId) {
        this.factorItemsId = factorItemsId;
    }

    public LongFilter getMaterialsId() {
        return materialsId;
    }

    public LongFilter materialsId() {
        if (materialsId == null) {
            materialsId = new LongFilter();
        }
        return materialsId;
    }

    public void setMaterialsId(LongFilter materialsId) {
        this.materialsId = materialsId;
    }

    public LongFilter getProducerPartyId() {
        return producerPartyId;
    }

    public LongFilter producerPartyId() {
        if (producerPartyId == null) {
            producerPartyId = new LongFilter();
        }
        return producerPartyId;
    }

    public void setProducerPartyId(LongFilter producerPartyId) {
        this.producerPartyId = producerPartyId;
    }

    public LongFilter getDesignerPartyId() {
        return designerPartyId;
    }

    public LongFilter designerPartyId() {
        if (designerPartyId == null) {
            designerPartyId = new LongFilter();
        }
        return designerPartyId;
    }

    public void setDesignerPartyId(LongFilter designerPartyId) {
        this.designerPartyId = designerPartyId;
    }

    public LongFilter getFoodTypeId() {
        return foodTypeId;
    }

    public LongFilter foodTypeId() {
        if (foodTypeId == null) {
            foodTypeId = new LongFilter();
        }
        return foodTypeId;
    }

    public void setFoodTypeId(LongFilter foodTypeId) {
        this.foodTypeId = foodTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FoodCriteria that = (FoodCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(foodCode, that.foodCode) &&
            Objects.equals(sizeClassId, that.sizeClassId) &&
            Objects.equals(categoryClassId, that.categoryClassId) &&
            Objects.equals(lastPrice, that.lastPrice) &&
            Objects.equals(description, that.description) &&
            Objects.equals(menuItemsId, that.menuItemsId) &&
            Objects.equals(factorItemsId, that.factorItemsId) &&
            Objects.equals(materialsId, that.materialsId) &&
            Objects.equals(producerPartyId, that.producerPartyId) &&
            Objects.equals(designerPartyId, that.designerPartyId) &&
            Objects.equals(foodTypeId, that.foodTypeId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            title,
            foodCode,
            sizeClassId,
            categoryClassId,
            lastPrice,
            description,
            menuItemsId,
            factorItemsId,
            materialsId,
            producerPartyId,
            designerPartyId,
            foodTypeId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FoodCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (foodCode != null ? "foodCode=" + foodCode + ", " : "") +
            (sizeClassId != null ? "sizeClassId=" + sizeClassId + ", " : "") +
            (categoryClassId != null ? "categoryClassId=" + categoryClassId + ", " : "") +
            (lastPrice != null ? "lastPrice=" + lastPrice + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (menuItemsId != null ? "menuItemsId=" + menuItemsId + ", " : "") +
            (factorItemsId != null ? "factorItemsId=" + factorItemsId + ", " : "") +
            (materialsId != null ? "materialsId=" + materialsId + ", " : "") +
            (producerPartyId != null ? "producerPartyId=" + producerPartyId + ", " : "") +
            (designerPartyId != null ? "designerPartyId=" + designerPartyId + ", " : "") +
            (foodTypeId != null ? "foodTypeId=" + foodTypeId + ", " : "") +
            "}";
    }
}
