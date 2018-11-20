package com.youngbrains.application.service;

import io.github.jhipster.service.filter.LongFilter;

import java.io.Serializable;

public class ReadBookCriteria implements Serializable {
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter bookId;

    private LongFilter profileId;

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
        return "ReadBookCriteria{" +
            "id=" + id +
            ", bookId=" + bookId +
            ", profileId=" + profileId +
            '}';
    }
}
