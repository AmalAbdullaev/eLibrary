package com.youngbrains.application.service.mapper;

import com.youngbrains.application.domain.*;
import com.youngbrains.application.service.dto.FavoriteBookDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity FavoriteBook and its DTO FavoriteBookDTO.
 */
@Mapper(componentModel = "spring", uses = {BookMapper.class, ProfileMapper.class})
public interface FavoriteBookMapper extends EntityMapper<FavoriteBookDTO, FavoriteBook> {

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    @Mapping(source = "book.coverPath", target = "bookCoverPath")
    @Mapping(source = "profile.id", target = "profileId")
    @Mapping(source = "book.authorFirstName", target = "bookAuthorFirstName")
    @Mapping(source = "book.authorLastName", target = "bookAuthorLastName")
    @Mapping(source = "book.genre.name", target = "bookGenreName")
    @Mapping(source = "book.pages", target = "bookPages")
    @Mapping(source = "book.description", target = "bookDescription")
    @Mapping(source = "book.path", target = "bookPath")
    @Mapping(source = "book.yearOfPublishing", target = "bookYearOfPublishing")
    @Mapping(source = "book.createdBy", target = "bookCreatedBy")
    FavoriteBookDTO toDto(FavoriteBook favoriteBook);

    @Mapping(source = "bookId", target = "book")
    @Mapping(source = "profileId", target = "profile")
    FavoriteBook toEntity(FavoriteBookDTO favoriteBookDTO);

    default FavoriteBook fromId(Long id) {
        if (id == null) {
            return null;
        }
        FavoriteBook favoriteBook = new FavoriteBook();
        favoriteBook.setId(id);
        return favoriteBook;
    }
}
