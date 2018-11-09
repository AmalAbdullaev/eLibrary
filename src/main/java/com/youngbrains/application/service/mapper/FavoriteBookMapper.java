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
    @Mapping(source = "profile.id", target = "profileId")
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
