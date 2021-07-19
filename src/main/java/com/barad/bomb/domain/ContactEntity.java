package com.barad.bomb.domain;

import com.barad.bomb.domain.enumeration.ContactType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 4 field fixed
 */
@Entity
@Table(name = "contact")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ContactEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "contact_type", nullable = false)
    private ContactType contactType;

    @NotNull
    @Size(max = 200)
    @Column(name = "contact_value", length = 200, nullable = false)
    private String contactValue;

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

    public ContactEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public ContactEntity title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ContactType getContactType() {
        return this.contactType;
    }

    public ContactEntity contactType(ContactType contactType) {
        this.contactType = contactType;
        return this;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    public String getContactValue() {
        return this.contactValue;
    }

    public ContactEntity contactValue(String contactValue) {
        this.contactValue = contactValue;
        return this;
    }

    public void setContactValue(String contactValue) {
        this.contactValue = contactValue;
    }

    public PartyEntity getParty() {
        return this.party;
    }

    public ContactEntity party(PartyEntity party) {
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
        if (!(o instanceof ContactEntity)) {
            return false;
        }
        return id != null && id.equals(((ContactEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactEntity{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", contactType='" + getContactType() + "'" +
            ", contactValue='" + getContactValue() + "'" +
            "}";
    }
}
