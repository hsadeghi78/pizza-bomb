package com.barad.bomb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 4 field fixed
 */
@Entity
@Table(name = "menu_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MenuItemEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @NotNull
    @Column(name = "expiration_date", nullable = false)
    private Instant expirationDate;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

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

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "menuItems", "factorItems", "materials", "producerParty", "designerParty", "foodType" },
        allowSetters = true
    )
    private FoodEntity food;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MenuItemEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public MenuItemEntity title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getExpirationDate() {
        return this.expirationDate;
    }

    public MenuItemEntity expirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getDescription() {
        return this.description;
    }

    public MenuItemEntity description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PartyEntity getParty() {
        return this.party;
    }

    public MenuItemEntity party(PartyEntity party) {
        this.setParty(party);
        return this;
    }

    public void setParty(PartyEntity party) {
        this.party = party;
    }

    public FoodEntity getFood() {
        return this.food;
    }

    public MenuItemEntity food(FoodEntity food) {
        this.setFood(food);
        return this;
    }

    public void setFood(FoodEntity food) {
        this.food = food;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MenuItemEntity)) {
            return false;
        }
        return id != null && id.equals(((MenuItemEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MenuItemEntity{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
