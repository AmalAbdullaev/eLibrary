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
