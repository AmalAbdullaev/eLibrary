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

    private String bookGenreName;

    private Integer bookPages;

    private String bookDescription;

    private String bookPath;

    private Integer bookYearOfPublishing;

    private String bookCreatedBy;
    
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
            "id=" + id +
            ", bookId=" + bookId +
            ", profileId=" + profileId +
            ", bookTitle='" + bookTitle + '\'' +
            ", bookAuthorFirstName='" + bookAuthorFirstName + '\'' +
            ", bookAuthorLastName='" + bookAuthorLastName + '\'' +
            ", bookGenreName='" + bookGenreName + '\'' +
            ", bookPages=" + bookPages +
            ", bookDescription='" + bookDescription + '\'' +
            ", bookPath='" + bookPath + '\'' +
            ", bookYearOfPublishing=" + bookYearOfPublishing +
            ", bookCreatedBy='" + bookCreatedBy + '\'' +
            ", bookCoverPath='" + bookCoverPath + '\'' +
            '}';
    }
}
