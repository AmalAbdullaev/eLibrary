package com.youngbrains.application.repository;

import com.youngbrains.application.domain.Book;
import com.youngbrains.application.domain.FavoriteBook;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.math.BigInteger;
import java.util.List;


/**
 * Spring Data JPA repository for the FavoriteBook entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FavoriteBookRepository extends JpaRepository<FavoriteBook, Long>, JpaSpecificationExecutor<FavoriteBook> {
    @Query(value = "select book_id from favorite_book group by book_id order by count(*) desc limit 10", nativeQuery = true)
    List<BigInteger> findTop10Books();
}
