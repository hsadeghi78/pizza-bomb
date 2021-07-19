package com.barad.bomb.domain;

import com.barad.bomb.domain.enumeration.PartyInfoType;
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
@Table(name = "party_information")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PartyInformationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "info_type", nullable = false)
    private PartyInfoType infoType;

    @NotNull
    @Size(max = 200)
    @Column(name = "info_title", length = 200, nullable = false)
    private String infoTitle;

    @Size(max = 2000)
    @Column(name = "info_desc", length = 2000)
    private String infoDesc;

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

    public PartyInformationEntity id(Long id) {
        this.id = id;
        return this;
    }

    public PartyInfoType getInfoType() {
        return this.infoType;
    }

    public PartyInformationEntity infoType(PartyInfoType infoType) {
        this.infoType = infoType;
        return this;
    }

    public void setInfoType(PartyInfoType infoType) {
        this.infoType = infoType;
    }

    public String getInfoTitle() {
        return this.infoTitle;
    }

    public PartyInformationEntity infoTitle(String infoTitle) {
        this.infoTitle = infoTitle;
        return this;
    }

    public void setInfoTitle(String infoTitle) {
        this.infoTitle = infoTitle;
    }

    public String getInfoDesc() {
        return this.infoDesc;
    }

    public PartyInformationEntity infoDesc(String infoDesc) {
        this.infoDesc = infoDesc;
        return this;
    }

    public void setInfoDesc(String infoDesc) {
        this.infoDesc = infoDesc;
    }

    public PartyEntity getParty() {
        return this.party;
    }

    public PartyInformationEntity party(PartyEntity party) {
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
        if (!(o instanceof PartyInformationEntity)) {
            return false;
        }
        return id != null && id.equals(((PartyInformationEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartyInformationEntity{" +
            "id=" + getId() +
            ", infoType='" + getInfoType() + "'" +
            ", infoTitle='" + getInfoTitle() + "'" +
            ", infoDesc='" + getInfoDesc() + "'" +
            "}";
    }
}
