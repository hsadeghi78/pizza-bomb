package com.barad.bomb.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.barad.bomb.domain.CommentEntity} entity.
 */
@ApiModel(description = "4 field fixed")
public class CommentDTO implements Serializable {

    private Long id;

    private Integer rating;

    @Size(max = 3000)
    private String description;

    private PartyDTO writerParty;

    private PartyDTO audienceParty;

    private CommentDTO parent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PartyDTO getWriterParty() {
        return writerParty;
    }

    public void setWriterParty(PartyDTO writerParty) {
        this.writerParty = writerParty;
    }

    public PartyDTO getAudienceParty() {
        return audienceParty;
    }

    public void setAudienceParty(PartyDTO audienceParty) {
        this.audienceParty = audienceParty;
    }

    public CommentDTO getParent() {
        return parent;
    }

    public void setParent(CommentDTO parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommentDTO)) {
            return false;
        }

        CommentDTO commentDTO = (CommentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentDTO{" +
            "id=" + getId() +
            ", rating=" + getRating() +
            ", description='" + getDescription() + "'" +
            ", writerParty=" + getWriterParty() +
            ", audienceParty=" + getAudienceParty() +
            ", parent=" + getParent() +
            "}";
    }
}
