package com.youngbrains.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Book.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "book")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "pages")
    private Integer pages;

    @NotNull
    @Column(name = "approved", nullable = false)
    private Boolean approved;

    @Column(name = "path")
    private String path;

    @Column(name = "cover_path")
    private String coverPath;

    @Column(name = "created_by")
    @CreatedBy
    @JsonIgnore
    private String createdBy;

    @Column(name = "created_date")
    @CreatedDate
    @JsonIgnore
    private Instant createdDate;

    @Column(name = "last_modified_by")
    @LastModifiedBy
    @JsonIgnore
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    @LastModifiedDate
    @JsonIgnore
    private Instant lastModifiedDate;

    @NotNull
    @Column(name = "year_of_publishing", nullable = false)
    private Integer yearOfPublishing;

    @NotNull
    @Column(name = "author_first_name", nullable = false)
    private String authorFirstName;

    @NotNull
    @Column(name = "author_last_name", nullable = false)
    private String authorLastName;

    @ManyToOne
    private Profile profile;

    @ManyToOne
    private Genre genre;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Book title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Book description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPages() {
        return pages;
    }

    public Book pages(Integer pages) {
        this.pages = pages;
        return this;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Boolean isApproved() {
        return approved;
    }

    public Book approved(Boolean approved) {
        this.approved = approved;
        return this;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public String getPath() {
        return path;
    }

    public Book path(String path) {
        this.path = path;
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public Book coverPath(String coverPath) {
        this.coverPath = coverPath;
        return this;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Book createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Book createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Book lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Book lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Integer getYearOfPublishing() {
        return yearOfPublishing;
    }

    public Book yearOfPublishing(Integer yearOfPublishing) {
        this.yearOfPublishing = yearOfPublishing;
        return this;
    }

    public void setYearOfPublishing(Integer yearOfPublishing) {
        this.yearOfPublishing = yearOfPublishing;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public Book authorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
        return this;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public Book authorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
        return this;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public Profile getProfile() {
        return profile;
    }

    public Book profile(Profile profile) {
        this.profile = profile;
        return this;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Genre getGenre() {
        return genre;
    }

    public Book genre(Genre genre) {
        this.genre = genre;
        return this;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Book book = (Book) o;
        if (book.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), book.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Book{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", pages=" + getPages() +
            ", approved='" + isApproved() + "'" +
            ", path='" + getPath() + "'" +
            ", coverPath='" + getCoverPath() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", yearOfPublishing=" + getYearOfPublishing() +
            ", authorFirstName='" + getAuthorFirstName() + "'" +
            ", authorLastName='" + getAuthorLastName() + "'" +
            "}";
    }
}
