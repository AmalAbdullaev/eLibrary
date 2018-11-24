package com.youngbrains.application.repository;

import com.youngbrains.application.domain.Book;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.math.BigInteger;
import java.util.List;


/**
 * Spring Data JPA repository for the Book entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Query(value = "select profile_id from book where approved = true group by profile_id order by count(*) desc limit 10", nativeQuery = true)
    List<BigInteger> findTop10Profiles();
    long countBookByGenreId(Long genreId);
    void deleteAllByProfileId(Long profileId);

}
