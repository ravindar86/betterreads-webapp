package io.javabrains.betterreads.model.userbooks;

import org.springframework.data.cassandra.core.mapping.*;

import java.time.LocalDate;

@Table(value = "books_read_by_userid")
public class UserBooks {

    @PrimaryKey
    private UserBooksPrimaryKey userBooksPrimaryKey;

    @Column("started_date")
    @CassandraType(type = CassandraType.Name.DATE)
    private LocalDate startedDate;

    @Column("completed_date")
    @CassandraType(type = CassandraType.Name.DATE)
    private LocalDate completedDate;

    @Column("reading_status")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String readingStatus;

    @Column("rating")
    @CassandraType(type = CassandraType.Name.INT)
    private Integer rating;

    public UserBooksPrimaryKey getUserBooksPrimaryKey() {
        return userBooksPrimaryKey;
    }

    public void setUserBooksPrimaryKey(UserBooksPrimaryKey userBooksPrimaryKey) {
        this.userBooksPrimaryKey = userBooksPrimaryKey;
    }

    public LocalDate getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(LocalDate startedDate) {
        this.startedDate = startedDate;
    }

    public LocalDate getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDate completedDate) {
        this.completedDate = completedDate;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReadingStatus() {
        return readingStatus;
    }

    public void setReadingStatus(String readingStatus) {
        this.readingStatus = readingStatus;
    }

    @Override
    public String toString() {
        return "UserBooks{" +
                "userBooksPrimaryKey=" + userBooksPrimaryKey +
                ", startedDate=" + startedDate +
                ", completedDate=" + completedDate +
                ", readingStatus='" + readingStatus + '\'' +
                ", rating=" + rating +
                '}';
    }
}
