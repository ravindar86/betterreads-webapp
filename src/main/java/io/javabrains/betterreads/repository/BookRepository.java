package io.javabrains.betterreads.repository;


import io.javabrains.betterreads.model.book.Book;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CassandraRepository<Book, String> {

}
