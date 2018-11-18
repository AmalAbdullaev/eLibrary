package com.youngbrains.application.service.dto;

import io.github.jhipster.service.filter.LongFilter;

import java.io.Serializable;

public class FavoriteBookCriteria implements Serializable {
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter bookId;

    private LongFilter profileId;

    FavoriteBookCriteria() {}

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getBookId() {
        return bookId;
    }

    public void setBookId(LongFilter bookId) {
        this.bookId = bookId;
    }

    public LongFilter getProfileId() {
        return profileId;
    }

    public void setProfileId(LongFilter profileId) {
        this.profileId = profileId;
    }

    @Override
    public String toString() {
        return "FavoriteBookCriteria{" +
            "id=" + id +
            ", bookId=" + bookId +
            ", profileId=" + profileId +
            '}';
    }
}
