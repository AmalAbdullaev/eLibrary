package com.youngbrains.application.repository;

import com.youngbrains.application.domain.ReadBook;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ReadBook entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReadBookRepository extends JpaRepository<ReadBook, Long> {
    void deleteAllByBookId(Long bookId);
}
