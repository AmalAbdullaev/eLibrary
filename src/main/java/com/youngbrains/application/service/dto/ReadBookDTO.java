package com.youngbrains.application.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the ReadBook entity.
 */
public class ReadBookDTO implements Serializable {

    private Long id;

    private Long bookId;

    private Long profileId;

    private String bookTitle;

    private String bookAuthorFirstName;

    private String bookAuthorLastName;
    
    private String bookCoverPath;

    public Long getId() {
        return id;
    }

    /**
     * @return the bookTitle
     */
    public String getBookTitle() {
        return bookTitle;
    }

    /**
     * @param bookTitle the bookTitle to set
     */
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    /**
     * @return the bookAuthorFirstName
     */
    public String getBookAuthorFirstName() {
        return bookAuthorFirstName;
    }

    /**
     * @param bookAuthorFirstName the bookAuthorFirstName to set
     */
    public void setBookAuthorFirstName(String bookAuthorFirstName) {
        this.bookAuthorFirstName = bookAuthorFirstName;
    }

    /**
     * @return the bookAuthorLastName
     */
    public String getBookAuthorLastName() {
        return bookAuthorLastName;
    }

    /**
     * @param bookAuthorLastName the bookAuthorLastName to set
     */
    public void setBookAuthorLastName(String bookAuthorLastName) {
        this.bookAuthorLastName = bookAuthorLastName;
    }

    /**
     * @return the bookCoverPath
     */
    public String getBookCoverPath() {
        return bookCoverPath;
    }

    /**
     * @param bookCoverPath the bookCoverPath to set
     */
    public void setBookCoverPath(String bookCoverPath) {
        this.bookCoverPath = bookCoverPath;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }





    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReadBookDTO readBookDTO = (ReadBookDTO) o;
        if(readBookDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), readBookDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReadBookDTO{" +
            "id=" + getId() +
            "}";
    }
}
