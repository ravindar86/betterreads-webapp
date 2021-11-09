package io.javabrains.betterreads.repository;

import io.javabrains.betterreads.model.user.BooksByUser;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface BooksByUserRepository extends CassandraRepository<BooksByUser, String> {

    Slice<BooksByUser> findAllById(String id, Pageable pageable);

    List<BooksByUser> findAllById(String id);

   // Slice<BooksByUser> findAllByIdOrderByTimeUuidDesc(String id, Pageable pageable);

    long deleteByIdAndBookId(String id, String bookId);
}
