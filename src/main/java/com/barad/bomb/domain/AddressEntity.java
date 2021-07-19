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
@Table(name = "address")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AddressEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @NotNull
    @Column(name = "lat", nullable = false)
    private Double lat;

    @NotNull
    @Column(name = "lon", nullable = false)
    private Double lon;

    @Size(max = 200)
    @Column(name = "street_1", length = 200)
    private String street1;

    @Size(max = 200)
    @Column(name = "street_2", length = 200)
    private String street2;

    @NotNull
    @Size(max = 1000)
    @Column(name = "address", length = 1000, nullable = false)
    private String address;

    @NotNull
    @Size(max = 12)
    @Column(name = "postal_code", length = 12, nullable = false)
    private String postalCode;

    @OneToMany(mappedBy = "deliveryAddress")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "factorItems", "buyerParty", "sellerParty", "deliveryAddress" }, allowSetters = true)
    private Set<FactorEntity> factors = new HashSet<>();

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

    public AddressEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public AddressEntity title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getLat() {
        return this.lat;
    }

    public AddressEntity lat(Double lat) {
        this.lat = lat;
        return this;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return this.lon;
    }

    public AddressEntity lon(Double lon) {
        this.lon = lon;
        return this;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getStreet1() {
        return this.street1;
    }

    public AddressEntity street1(String street1) {
        this.street1 = street1;
        return this;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return this.street2;
    }

    public AddressEntity street2(String street2) {
        this.street2 = street2;
        return this;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getAddress() {
        return this.address;
    }

    public AddressEntity address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public AddressEntity postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Set<FactorEntity> getFactors() {
        return this.factors;
    }

    public AddressEntity factors(Set<FactorEntity> factors) {
        this.setFactors(factors);
        return this;
    }

    public AddressEntity addFactors(FactorEntity factor) {
        this.factors.add(factor);
        factor.setDeliveryAddress(this);
        return this;
    }

    public AddressEntity removeFactors(FactorEntity factor) {
        this.factors.remove(factor);
        factor.setDeliveryAddress(null);
        return this;
    }

    public void setFactors(Set<FactorEntity> factors) {
        if (this.factors != null) {
            this.factors.forEach(i -> i.setDeliveryAddress(null));
        }
        if (factors != null) {
            factors.forEach(i -> i.setDeliveryAddress(this));
        }
        this.factors = factors;
    }

    public PartyEntity getParty() {
        return this.party;
    }

    public AddressEntity party(PartyEntity party) {
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
        if (!(o instanceof AddressEntity)) {
            return false;
        }
        return id != null && id.equals(((AddressEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AddressEntity{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", lat=" + getLat() +
            ", lon=" + getLon() +
            ", street1='" + getStreet1() + "'" +
            ", street2='" + getStreet2() + "'" +
            ", address='" + getAddress() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            "}";
    }
}
