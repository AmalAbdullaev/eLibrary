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

    private String bookAuthorFirstName;

    private String bookAuthorLastName;

    private String bookGenreName;

    private Integer bookPages;

    private String bookDescription;

    private String bookPath;

    private Integer bookYearOfPublishing;

    private String bookCreatedBy;


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

    public String getBookAuthorFirstName() {
        return bookAuthorFirstName;
    }

    public void setBookAuthorFirstName(String bookAuthorFirstName) {
        this.bookAuthorFirstName = bookAuthorFirstName;
    }

    public String getBookAuthorLastName() {
        return bookAuthorLastName;
    }

    public void setBookAuthorLastName(String bookAuthorLastName) {
        this.bookAuthorLastName = bookAuthorLastName;
    }

    public String getBookGenreName() {
        return bookGenreName;
    }

    public void setBookGenreName(String bookGenreName) {
        this.bookGenreName = bookGenreName;
    }

    public Integer getBookPages() {
        return bookPages;
    }

    public void setBookPages(Integer bookPages) {
        this.bookPages = bookPages;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public String getBookPath() {
        return bookPath;
    }

    public void setBookPath(String bookPath) {
        this.bookPath = bookPath;
    }

    public Integer getBookYearOfPublishing() {
        return bookYearOfPublishing;
    }

    public void setBookYearOfPublishing(Integer bookYearOfPublishing) {
        this.bookYearOfPublishing = bookYearOfPublishing;
    }

    public String getBookCreatedBy() {
        return bookCreatedBy;
    }

    public void setBookCreatedBy(String bookCreatedBy) {
        this.bookCreatedBy = bookCreatedBy;
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
            "id=" + id +
            ", bookId=" + bookId +
            ", profileId=" + profileId +
            ", bookCoverPath='" + bookCoverPath + '\'' +
            ", bookTitle='" + bookTitle + '\'' +
            ", bookAuthorFirstName='" + bookAuthorFirstName + '\'' +
            ", bookAuthorLastName='" + bookAuthorLastName + '\'' +
            ", bookGenreName='" + bookGenreName + '\'' +
            ", bookPages=" + bookPages +
            ", bookDescription='" + bookDescription + '\'' +
            ", bookPath='" + bookPath + '\'' +
            ", bookYearOfPublishing=" + bookYearOfPublishing +
            ", bookCreatedBy='" + bookCreatedBy + '\'' +
            '}';
    }
}
