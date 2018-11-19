package com.youngbrains.application.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the FavoriteBook entity.
 */
public class FavoriteBookDTO implements Serializable {

    private Long id;

    private Long bookId;

    private Long profileId;

    private String bookCoverPath;

    private String bookTitle;


    public void setBookTitle(String bookTitle){
        this.bookTitle = bookTitle;
    }

    public String getBookTitle(){
        return bookTitle;
    }

    public String getBookCoverPath(){
        return bookCoverPath;
    }

    public void setBookCoverPath(String bookCoverPath){
        this.bookCoverPath = bookCoverPath;
    }

    public Long getId() {
        return id;
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

        FavoriteBookDTO favoriteBookDTO = (FavoriteBookDTO) o;
        if(favoriteBookDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), favoriteBookDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FavoriteBookDTO{" +
            "id=" + getId() +
            "}";
    }
}
