package io.javabrains.betterreads.repository;

import io.javabrains.betterreads.model.userbooks.UserBooks;
import io.javabrains.betterreads.model.userbooks.UserBooksPrimaryKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBooksRepository extends CassandraRepository<UserBooks, UserBooksPrimaryKey> {
}
