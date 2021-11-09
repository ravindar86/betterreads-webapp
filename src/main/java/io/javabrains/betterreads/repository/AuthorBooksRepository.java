package io.javabrains.betterreads.repository;

import io.javabrains.betterreads.model.authorbooks.AuthorBooks;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorBooksRepository extends CassandraRepository<AuthorBooks,String> {

    @Query("SELECT * FROM author_by_books WHERE author_name = ?0 ALLOW FILTERING")
    List<AuthorBooks> findAllByName(String authorName);
}
