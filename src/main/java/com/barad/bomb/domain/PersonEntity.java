package com.barad.bomb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
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
@Table(name = "person")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PersonEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "fisrt_name", length = 200, nullable = false)
    private String fisrtName;

    @NotNull
    @Size(max = 200)
    @Column(name = "last_name", length = 200, nullable = false)
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @NotNull
    @Size(max = 10)
    @Column(name = "national_code", length = 10, nullable = false, unique = true)
    private String nationalCode;

    @OneToMany(mappedBy = "person")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Set<PartyEntity> parties = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PersonEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getFisrtName() {
        return this.fisrtName;
    }

    public PersonEntity fisrtName(String fisrtName) {
        this.fisrtName = fisrtName;
        return this;
    }

    public void setFisrtName(String fisrtName) {
        this.fisrtName = fisrtName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public PersonEntity lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public PersonEntity birthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getNationalCode() {
        return this.nationalCode;
    }

    public PersonEntity nationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
        return this;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public Set<PartyEntity> getParties() {
        return this.parties;
    }

    public PersonEntity parties(Set<PartyEntity> parties) {
        this.setParties(parties);
        return this;
    }

    public PersonEntity addParties(PartyEntity party) {
        this.parties.add(party);
        party.setPerson(this);
        return this;
    }

    public PersonEntity removeParties(PartyEntity party) {
        this.parties.remove(party);
        party.setPerson(null);
        return this;
    }

    public void setParties(Set<PartyEntity> parties) {
        if (this.parties != null) {
            this.parties.forEach(i -> i.setPerson(null));
        }
        if (parties != null) {
            parties.forEach(i -> i.setPerson(this));
        }
        this.parties = parties;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonEntity)) {
            return false;
        }
        return id != null && id.equals(((PersonEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonEntity{" +
            "id=" + getId() +
            ", fisrtName='" + getFisrtName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", nationalCode='" + getNationalCode() + "'" +
            "}";
    }
}
