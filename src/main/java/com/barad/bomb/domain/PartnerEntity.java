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
@Table(name = "partner")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PartnerEntity implements Serializable {

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
    @Column(name = "partner_code", length = 100, nullable = false, unique = true)
    private String partnerCode;

    @NotNull
    @Size(max = 200)
    @Column(name = "trade_title", length = 200, nullable = false)
    private String tradeTitle;

    @Size(max = 100)
    @Column(name = "economic_code", length = 100, unique = true)
    private String economicCode;

    @Column(name = "activity_date")
    private LocalDate activityDate;

    @OneToMany(mappedBy = "partner")
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

    public PartnerEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public PartnerEntity title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPartnerCode() {
        return this.partnerCode;
    }

    public PartnerEntity partnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
        return this;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getTradeTitle() {
        return this.tradeTitle;
    }

    public PartnerEntity tradeTitle(String tradeTitle) {
        this.tradeTitle = tradeTitle;
        return this;
    }

    public void setTradeTitle(String tradeTitle) {
        this.tradeTitle = tradeTitle;
    }

    public String getEconomicCode() {
        return this.economicCode;
    }

    public PartnerEntity economicCode(String economicCode) {
        this.economicCode = economicCode;
        return this;
    }

    public void setEconomicCode(String economicCode) {
        this.economicCode = economicCode;
    }

    public LocalDate getActivityDate() {
        return this.activityDate;
    }

    public PartnerEntity activityDate(LocalDate activityDate) {
        this.activityDate = activityDate;
        return this;
    }

    public void setActivityDate(LocalDate activityDate) {
        this.activityDate = activityDate;
    }

    public Set<PartyEntity> getParties() {
        return this.parties;
    }

    public PartnerEntity parties(Set<PartyEntity> parties) {
        this.setParties(parties);
        return this;
    }

    public PartnerEntity addParties(PartyEntity party) {
        this.parties.add(party);
        party.setPartner(this);
        return this;
    }

    public PartnerEntity removeParties(PartyEntity party) {
        this.parties.remove(party);
        party.setPartner(null);
        return this;
    }

    public void setParties(Set<PartyEntity> parties) {
        if (this.parties != null) {
            this.parties.forEach(i -> i.setPartner(null));
        }
        if (parties != null) {
            parties.forEach(i -> i.setPartner(this));
        }
        this.parties = parties;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PartnerEntity)) {
            return false;
        }
        return id != null && id.equals(((PartnerEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartnerEntity{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", partnerCode='" + getPartnerCode() + "'" +
            ", tradeTitle='" + getTradeTitle() + "'" +
            ", economicCode='" + getEconomicCode() + "'" +
            ", activityDate='" + getActivityDate() + "'" +
            "}";
    }
}
