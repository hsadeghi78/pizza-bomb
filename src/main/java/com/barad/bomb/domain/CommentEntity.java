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
@Table(name = "comment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CommentEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rating")
    private Integer rating;

    @Size(max = 3000)
    @Column(name = "description", length = 3000)
    private String description;

    @OneToMany(mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "children", "writerParty", "audienceParty", "parent" }, allowSetters = true)
    private Set<CommentEntity> children = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "criticisms",
            "files",
            "moreInfos",
            "writtenComments",
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
    private PartyEntity writerParty;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "criticisms",
            "files",
            "moreInfos",
            "writtenComments",
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
    private PartyEntity audienceParty;

    @ManyToOne
    @JsonIgnoreProperties(value = { "children", "writerParty", "audienceParty", "parent" }, allowSetters = true)
    private CommentEntity parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CommentEntity id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRating() {
        return this.rating;
    }

    public CommentEntity rating(Integer rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return this.description;
    }

    public CommentEntity description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<CommentEntity> getChildren() {
        return this.children;
    }

    public CommentEntity children(Set<CommentEntity> comments) {
        this.setChildren(comments);
        return this;
    }

    public CommentEntity addChildren(CommentEntity comment) {
        this.children.add(comment);
        comment.setParent(this);
        return this;
    }

    public CommentEntity removeChildren(CommentEntity comment) {
        this.children.remove(comment);
        comment.setParent(null);
        return this;
    }

    public void setChildren(Set<CommentEntity> comments) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setParent(this));
        }
        this.children = comments;
    }

    public PartyEntity getWriterParty() {
        return this.writerParty;
    }

    public CommentEntity writerParty(PartyEntity party) {
        this.setWriterParty(party);
        return this;
    }

    public void setWriterParty(PartyEntity party) {
        this.writerParty = party;
    }

    public PartyEntity getAudienceParty() {
        return this.audienceParty;
    }

    public CommentEntity audienceParty(PartyEntity party) {
        this.setAudienceParty(party);
        return this;
    }

    public void setAudienceParty(PartyEntity party) {
        this.audienceParty = party;
    }

    public CommentEntity getParent() {
        return this.parent;
    }

    public CommentEntity parent(CommentEntity comment) {
        this.setParent(comment);
        return this;
    }

    public void setParent(CommentEntity comment) {
        this.parent = comment;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommentEntity)) {
            return false;
        }
        return id != null && id.equals(((CommentEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentEntity{" +
            "id=" + getId() +
            ", rating=" + getRating() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
