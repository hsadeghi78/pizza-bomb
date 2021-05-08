package com.barad.bomb.domain;

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
@Table(name = "criticism")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CriticismEntity extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 150)
    @Column(name = "full_name", length = 150, nullable = false)
    private String fullName;

    @Size(max = 150)
    @Column(name = "email", length = 150)
    private String email;

    @Size(max = 15)
    @Column(name = "contact_number", length = 15)
    private String contactNumber;

    @NotNull
    @Size(max = 3000)
    @Column(name = "description", length = 3000, nullable = false)
    private String description;

    @ManyToOne
    @JsonIgnoreProperties(value = { "branchs", "criticisms" }, allowSetters = true)
    private PartyEntity party;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CriticismEntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getFullName() {
        return this.fullName;
    }

    public CriticismEntity fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return this.email;
    }

    public CriticismEntity email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return this.contactNumber;
    }

    public CriticismEntity contactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
        return this;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getDescription() {
        return this.description;
    }

    public CriticismEntity description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PartyEntity getParty() {
        return this.party;
    }

    public CriticismEntity party(PartyEntity party) {
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
        if (!(o instanceof CriticismEntity)) {
            return false;
        }
        return id != null && id.equals(((CriticismEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CriticismEntity{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", email='" + getEmail() + "'" +
            ", contactNumber='" + getContactNumber() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
