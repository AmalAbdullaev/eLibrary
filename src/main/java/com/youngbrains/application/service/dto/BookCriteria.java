package com.youngbrains.application.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Book entity. This class is used in BookResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /books?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BookCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter title;

    private IntegerFilter pages;

    private BooleanFilter approved;

    private StringFilter path;

    private StringFilter coverPath;

    private IntegerFilter yearOfPublishing;

    private StringFilter authorFirstName;

    private StringFilter authorLastName;

    private LongFilter profileId;

    private LongFilter genreId;

    public BookCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public IntegerFilter getPages() {
        return pages;
    }

    public void setPages(IntegerFilter pages) {
        this.pages = pages;
    }

    public BooleanFilter getApproved() {
        return approved;
    }

    public void setApproved(BooleanFilter approved) {
        this.approved = approved;
    }

    public StringFilter getPath() {
        return path;
    }

    public void setPath(StringFilter path) {
        this.path = path;
    }

    public StringFilter getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(StringFilter coverPath) {
        this.coverPath = coverPath;
    }

    public IntegerFilter getYearOfPublishing() {
        return yearOfPublishing;
    }

    public void setYearOfPublishing(IntegerFilter yearOfPublishing) {
        this.yearOfPublishing = yearOfPublishing;
    }

    public StringFilter getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(StringFilter authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public StringFilter getAuthorLastName() {
        return authorLastName;
    }

    public void setAuthorLastName(StringFilter authorLastName) {
        this.authorLastName = authorLastName;
    }

    public LongFilter getProfileId() {
        return profileId;
    }

    public void setProfileId(LongFilter profileId) {
        this.profileId = profileId;
    }

    public LongFilter getGenreId() {
        return genreId;
    }

    public void setGenreId(LongFilter genreId) {
        this.genreId = genreId;
    }

    @Override
    public String toString() {
        return "BookCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (pages != null ? "pages=" + pages + ", " : "") +
                (approved != null ? "approved=" + approved + ", " : "") +
                (path != null ? "path=" + path + ", " : "") +
                (coverPath != null ? "coverPath=" + coverPath + ", " : "") +
                (yearOfPublishing != null ? "yearOfPublishing=" + yearOfPublishing + ", " : "") +
                (authorFirstName != null ? "authorFirstName=" + authorFirstName + ", " : "") +
                (authorLastName != null ? "authorLastName=" + authorLastName + ", " : "") +
                (profileId != null ? "profileId=" + profileId + ", " : "") +
                (genreId != null ? "genreId=" + genreId + ", " : "") +
            "}";
    }

}
