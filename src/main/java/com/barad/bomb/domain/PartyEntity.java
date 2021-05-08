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
@Table(name = "party")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PartyEntity extends AbstractAuditingEntity implements Serializable {

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
    @Column(name = "party_code", length = 100, nullable = false, unique = true)
    private String partyCode;

    @NotNull
    @Size(max = 200)
    @Column(name = "trade_title", length = 200, nullable = false, unique = true)
    private String tradeTitle;

    @NotNull
    @Column(name = "activation_date", nullable = false)
    private LocalDate activationDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @NotNull
    @Column(name = "activation_status", nullable = false)
    private Boolean activationStatus;

    @Size(max = 3000)
    @Column(name = "description", length = 3000)
    private String description;

    @OneToMany(mappedBy = "party")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "party" }, allowSetters = true)
    private Set<BranchEntity> branchs = new HashSet<>();

    @OneToMany(mappedBy = "party")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "party" }, allowSetters = true)
    private Set<CriticismEntity> criticisms = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PartyEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public PartyEntity title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPartyCode() {
        return this.partyCode;
    }

    public PartyEntity partyCode(String partyCode) {
        this.partyCode = partyCode;
        return this;
    }

    public void setPartyCode(String partyCode) {
        this.partyCode = partyCode;
    }

    public String getTradeTitle() {
        return this.tradeTitle;
    }

    public PartyEntity tradeTitle(String tradeTitle) {
        this.tradeTitle = tradeTitle;
        return this;
    }

    public void setTradeTitle(String tradeTitle) {
        this.tradeTitle = tradeTitle;
    }

    public LocalDate getActivationDate() {
        return this.activationDate;
    }

    public PartyEntity activationDate(LocalDate activationDate) {
        this.activationDate = activationDate;
        return this;
    }

    public void setActivationDate(LocalDate activationDate) {
        this.activationDate = activationDate;
    }

    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    public PartyEntity expirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Boolean getActivationStatus() {
        return this.activationStatus;
    }

    public PartyEntity activationStatus(Boolean activationStatus) {
        this.activationStatus = activationStatus;
        return this;
    }

    public void setActivationStatus(Boolean activationStatus) {
        this.activationStatus = activationStatus;
    }

    public String getDescription() {
        return this.description;
    }

    public PartyEntity description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<BranchEntity> getBranchs() {
        return this.branchs;
    }

    public PartyEntity branchs(Set<BranchEntity> branches) {
        this.setBranchs(branches);
        return this;
    }

    public PartyEntity addBranchs(BranchEntity branch) {
        this.branchs.add(branch);
        branch.setParty(this);
        return this;
    }

    public PartyEntity removeBranchs(BranchEntity branch) {
        this.branchs.remove(branch);
        branch.setParty(null);
        return this;
    }

    public void setBranchs(Set<BranchEntity> branches) {
        if (this.branchs != null) {
            this.branchs.forEach(i -> i.setParty(null));
        }
        if (branches != null) {
            branches.forEach(i -> i.setParty(this));
        }
        this.branchs = branches;
    }

    public Set<CriticismEntity> getCriticisms() {
        return this.criticisms;
    }

    public PartyEntity criticisms(Set<CriticismEntity> criticisms) {
        this.setCriticisms(criticisms);
        return this;
    }

    public PartyEntity addCriticisms(CriticismEntity criticism) {
        this.criticisms.add(criticism);
        criticism.setParty(this);
        return this;
    }

    public PartyEntity removeCriticisms(CriticismEntity criticism) {
        this.criticisms.remove(criticism);
        criticism.setParty(null);
        return this;
    }

    public void setCriticisms(Set<CriticismEntity> criticisms) {
        if (this.criticisms != null) {
            this.criticisms.forEach(i -> i.setParty(null));
        }
        if (criticisms != null) {
            criticisms.forEach(i -> i.setParty(this));
        }
        this.criticisms = criticisms;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PartyEntity)) {
            return false;
        }
        return id != null && id.equals(((PartyEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartyEntity{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", partyCode='" + getPartyCode() + "'" +
            ", tradeTitle='" + getTradeTitle() + "'" +
            ", activationDate='" + getActivationDate() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", activationStatus='" + getActivationStatus() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
