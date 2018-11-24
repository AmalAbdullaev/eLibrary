package com.youngbrains.application.service.mapper;

import com.youngbrains.application.domain.*;
import com.youngbrains.application.service.dto.ReadBookDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ReadBook and its DTO ReadBookDTO.
 */
@Mapper(componentModel = "spring", uses = {BookMapper.class, ProfileMapper.class})
public interface ReadBookMapper extends EntityMapper<ReadBookDTO, ReadBook> {

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    @Mapping(source = "book.authorFirstName", target = "bookAuthorFirstName")
    @Mapping(source = "book.authorLastName", target = "bookAuthorLastName")
    @Mapping(source = "book.coverPath", target = "bookCoverPath")
    @Mapping(source = "book.genre.name", target = "bookGenreName")
    @Mapping(source = "book.pages", target = "bookPages")
    @Mapping(source = "book.description", target = "bookDescription")
    @Mapping(source = "book.path", target = "bookPath")
    @Mapping(source = "book.yearOfPublishing", target = "bookYearOfPublishing")
    @Mapping(source = "book.createdBy", target = "bookCreatedBy")
    @Mapping(source = "profile.id", target = "profileId")
    ReadBookDTO toDto(ReadBook readBook);

    @Mapping(source = "bookId", target = "book")
    @Mapping(source = "profileId", target = "profile")
    ReadBook toEntity(ReadBookDTO readBookDTO);

    default ReadBook fromId(Long id) {
        if (id == null) {
            return null;
        }
        ReadBook readBook = new ReadBook();
        readBook.setId(id);
        return readBook;
    }
}
