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
 * 4 field fixed sample:PIZZA, SANDWICH, SNACK, BURGER, SOKHARI
 */
@Entity
@Table(name = "food_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FoodTypeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @NotNull
    @Size(max = 50)
    @Column(name = "type_code", length = 50, nullable = false, unique = true)
    private String typeCode;

    @Size(max = 3000)
    @Column(name = "description", length = 3000)
    private String description;

    @OneToMany(mappedBy = "foodType")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "menuItems", "factorItems", "materials", "producerParty", "designerParty", "foodType" },
        allowSetters = true
    )
    private Set<FoodEntity> foods = new HashSet<>();

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
    private PartyEntity party;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FoodTypeEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public FoodTypeEntity title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTypeCode() {
        return this.typeCode;
    }

    public FoodTypeEntity typeCode(String typeCode) {
        this.typeCode = typeCode;
        return this;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getDescription() {
        return this.description;
    }

    public FoodTypeEntity description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<FoodEntity> getFoods() {
        return this.foods;
    }

    public FoodTypeEntity foods(Set<FoodEntity> foods) {
        this.setFoods(foods);
        return this;
    }

    public FoodTypeEntity addFoods(FoodEntity food) {
        this.foods.add(food);
        food.setFoodType(this);
        return this;
    }

    public FoodTypeEntity removeFoods(FoodEntity food) {
        this.foods.remove(food);
        food.setFoodType(null);
        return this;
    }

    public void setFoods(Set<FoodEntity> foods) {
        if (this.foods != null) {
            this.foods.forEach(i -> i.setFoodType(null));
        }
        if (foods != null) {
            foods.forEach(i -> i.setFoodType(this));
        }
        this.foods = foods;
    }

    public PartyEntity getParty() {
        return this.party;
    }

    public FoodTypeEntity party(PartyEntity party) {
        this.setParty(party);
        return this;
    }

    public void setParty(PartyEntity party) {
        this.party = party;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FoodTypeEntity)) {
            return false;
        }
        return id != null && id.equals(((FoodTypeEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FoodTypeEntity{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", typeCode='" + getTypeCode() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
