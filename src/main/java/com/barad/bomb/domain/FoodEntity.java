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
 * 4 field fixed
 */
@Entity
@Table(name = "food")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FoodEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @NotNull
    @Size(max = 100)
    @Column(name = "food_code", length = 100, nullable = false, unique = true)
    private String foodCode;

    @Column(name = "size_class_id")
    private Long sizeClassId;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    /**
     * for Appetizer, Main Course, Desert, Drink
     */
    @Column(name = "category_class_id")
    private Long categoryClassId;

    @NotNull
    @Column(name = "last_price", nullable = false)
    private Double lastPrice;

    @Size(max = 3000)
    @Column(name = "description", length = 3000)
    private String description;

    @OneToMany(mappedBy = "food")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "party", "food" }, allowSetters = true)
    private Set<MenuItemEntity> menuItems = new HashSet<>();

    @OneToMany(mappedBy = "food")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "food", "factor" }, allowSetters = true)
    private Set<FactorItemEntity> factorItems = new HashSet<>();

    @OneToMany(mappedBy = "food")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "food" }, allowSetters = true)
    private Set<ConsumeMaterialEntity> materials = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "criticisms",
            "files",
            "moreInfos",
            "writedComments",
            "audienceComments",
            "foodTypes",
            "children",
            "contacts",
            "addresses",
            "menuItems",
            "produceFoods",
            "designedFoods",
            "buyerFactors",
            "sellerFactors",
            "parent",
            "partner",
            "person",
        },
        allowSetters = true
    )
    private PartyEntity producerParty;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "criticisms",
            "files",
            "moreInfos",
            "writedComments",
            "audienceComments",
            "foodTypes",
            "children",
            "contacts",
            "addresses",
            "menuItems",
            "produceFoods",
            "designedFoods",
            "buyerFactors",
            "sellerFactors",
            "parent",
            "partner",
            "person",
        },
        allowSetters = true
    )
    private PartyEntity designerParty;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "foods", "party" }, allowSetters = true)
    private FoodTypeEntity foodType;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FoodEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public FoodEntity title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFoodCode() {
        return this.foodCode;
    }

    public FoodEntity foodCode(String foodCode) {
        this.foodCode = foodCode;
        return this;
    }

    public void setFoodCode(String foodCode) {
        this.foodCode = foodCode;
    }

    public Long getSizeClassId() {
        return this.sizeClassId;
    }

    public FoodEntity sizeClassId(Long sizeClassId) {
        this.sizeClassId = sizeClassId;
        return this;
    }

    public void setSizeClassId(Long sizeClassId) {
        this.sizeClassId = sizeClassId;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public FoodEntity photo(byte[] photo) {
        this.photo = photo;
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return this.photoContentType;
    }

    public FoodEntity photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public Long getCategoryClassId() {
        return this.categoryClassId;
    }

    public FoodEntity categoryClassId(Long categoryClassId) {
        this.categoryClassId = categoryClassId;
        return this;
    }

    public void setCategoryClassId(Long categoryClassId) {
        this.categoryClassId = categoryClassId;
    }

    public Double getLastPrice() {
        return this.lastPrice;
    }

    public FoodEntity lastPrice(Double lastPrice) {
        this.lastPrice = lastPrice;
        return this;
    }

    public void setLastPrice(Double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public String getDescription() {
        return this.description;
    }

    public FoodEntity description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<MenuItemEntity> getMenuItems() {
        return this.menuItems;
    }

    public FoodEntity menuItems(Set<MenuItemEntity> menuItems) {
        this.setMenuItems(menuItems);
        return this;
    }

    public FoodEntity addMenuItems(MenuItemEntity menuItem) {
        this.menuItems.add(menuItem);
        menuItem.setFood(this);
        return this;
    }

    public FoodEntity removeMenuItems(MenuItemEntity menuItem) {
        this.menuItems.remove(menuItem);
        menuItem.setFood(null);
        return this;
    }

    public void setMenuItems(Set<MenuItemEntity> menuItems) {
        if (this.menuItems != null) {
            this.menuItems.forEach(i -> i.setFood(null));
        }
        if (menuItems != null) {
            menuItems.forEach(i -> i.setFood(this));
        }
        this.menuItems = menuItems;
    }

    public Set<FactorItemEntity> getFactorItems() {
        return this.factorItems;
    }

    public FoodEntity factorItems(Set<FactorItemEntity> factorItems) {
        this.setFactorItems(factorItems);
        return this;
    }

    public FoodEntity addFactorItems(FactorItemEntity factorItem) {
        this.factorItems.add(factorItem);
        factorItem.setFood(this);
        return this;
    }

    public FoodEntity removeFactorItems(FactorItemEntity factorItem) {
        this.factorItems.remove(factorItem);
        factorItem.setFood(null);
        return this;
    }

    public void setFactorItems(Set<FactorItemEntity> factorItems) {
        if (this.factorItems != null) {
            this.factorItems.forEach(i -> i.setFood(null));
        }
        if (factorItems != null) {
            factorItems.forEach(i -> i.setFood(this));
        }
        this.factorItems = factorItems;
    }

    public Set<ConsumeMaterialEntity> getMaterials() {
        return this.materials;
    }

    public FoodEntity materials(Set<ConsumeMaterialEntity> consumeMaterials) {
        this.setMaterials(consumeMaterials);
        return this;
    }

    public FoodEntity addMaterials(ConsumeMaterialEntity consumeMaterial) {
        this.materials.add(consumeMaterial);
        consumeMaterial.setFood(this);
        return this;
    }

    public FoodEntity removeMaterials(ConsumeMaterialEntity consumeMaterial) {
        this.materials.remove(consumeMaterial);
        consumeMaterial.setFood(null);
        return this;
    }

    public void setMaterials(Set<ConsumeMaterialEntity> consumeMaterials) {
        if (this.materials != null) {
            this.materials.forEach(i -> i.setFood(null));
        }
        if (consumeMaterials != null) {
            consumeMaterials.forEach(i -> i.setFood(this));
        }
        this.materials = consumeMaterials;
    }

    public PartyEntity getProducerParty() {
        return this.producerParty;
    }

    public FoodEntity producerParty(PartyEntity party) {
        this.setProducerParty(party);
        return this;
    }

    public void setProducerParty(PartyEntity party) {
        this.producerParty = party;
    }

    public PartyEntity getDesignerParty() {
        return this.designerParty;
    }

    public FoodEntity designerParty(PartyEntity party) {
        this.setDesignerParty(party);
        return this;
    }

    public void setDesignerParty(PartyEntity party) {
        this.designerParty = party;
    }

    public FoodTypeEntity getFoodType() {
        return this.foodType;
    }

    public FoodEntity foodType(FoodTypeEntity foodType) {
        this.setFoodType(foodType);
        return this;
    }

    public void setFoodType(FoodTypeEntity foodType) {
        this.foodType = foodType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FoodEntity)) {
            return false;
        }
        return id != null && id.equals(((FoodEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FoodEntity{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", foodCode='" + getFoodCode() + "'" +
            ", sizeClassId=" + getSizeClassId() +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            ", categoryClassId=" + getCategoryClassId() +
            ", lastPrice=" + getLastPrice() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
