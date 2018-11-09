package com.youngbrains.application.service.mapper;

import com.youngbrains.application.domain.*;
import com.youngbrains.application.service.dto.BookDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Book and its DTO BookDTO.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class, GenreMapper.class})
public interface BookMapper extends EntityMapper<BookDTO, Book> {

    @Mapping(source = "profile.id", target = "profileId")
    @Mapping(source = "genre.id", target = "genreId")
    @Mapping(source = "genre.name", target = "genreName")
    BookDTO toDto(Book book);

    @Mapping(source = "profileId", target = "profile")
    @Mapping(source = "genreId", target = "genre")
    Book toEntity(BookDTO bookDTO);

    default Book fromId(Long id) {
        if (id == null) {
            return null;
        }
        Book book = new Book();
        book.setId(id);
        return book;
    }
}
