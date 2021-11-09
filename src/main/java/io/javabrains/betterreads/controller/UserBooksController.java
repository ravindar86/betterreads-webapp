package io.javabrains.betterreads.controller;

import io.javabrains.betterreads.model.book.Book;
import io.javabrains.betterreads.model.user.BooksByUser;
import io.javabrains.betterreads.model.userbooks.UserBooks;
import io.javabrains.betterreads.model.userbooks.UserBooksPrimaryKey;
import io.javabrains.betterreads.repository.BookRepository;
import io.javabrains.betterreads.repository.BooksByUserRepository;
import io.javabrains.betterreads.repository.UserBooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class UserBooksController {

    @Autowired
    private UserBooksRepository userBooksRepository;

    @Autowired
    BooksByUserRepository booksByUserRepository;

    @Autowired
    BookRepository bookRepository;

    @PostMapping("/addUserBook")
    public ModelAndView addBookForUser(@RequestBody MultiValueMap<String, String> formData,
                                       @AuthenticationPrincipal OAuth2User principal){

        if(principal==null || principal.getAttribute("login")==null)
            return null;

        String bookId = formData.getFirst("bookId");
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (!optionalBook.isPresent()) {
            return new ModelAndView("redirect:/");
        }
        Book book = optionalBook.get();

        UserBooks userBooks = new UserBooks();
        String userId = principal.getAttribute("login");

        UserBooksPrimaryKey userBooksPrimaryKey = new UserBooksPrimaryKey();
        userBooksPrimaryKey.setBookId(bookId);
        userBooksPrimaryKey.setUserId(userId);
        userBooks.setUserBooksPrimaryKey(userBooksPrimaryKey);
        int rating = Integer.parseInt(formData.getFirst("rating"));

        userBooks.setStartedDate(LocalDate.parse(formData.getFirst("startDate")));
        userBooks.setCompletedDate(LocalDate.parse(formData.getFirst("completedDate")));
        userBooks.setRating(rating);
        userBooks.setReadingStatus(formData.getFirst("readingStatus"));

        userBooksRepository.save(userBooks);

        // delete the records for the specific book before adding the latest details
        List<BooksByUser> booksList
                = booksByUserRepository.findAllById(userId);
        for(BooksByUser booksByUser : booksList) {
            if(booksByUser.getBookId().equals(bookId)) {
                booksByUserRepository.delete(booksByUser);
            }
        }

        // saving the details of the book for the specific user
        BooksByUser booksByUser = new BooksByUser();
        booksByUser.setId(userId);
        booksByUser.setBookId(bookId);
        booksByUser.setBookName(book.getName());
        booksByUser.setCoverIds(book.getCoverIds());
        booksByUser.setAuthorNames(book.getAuthorNames());
        booksByUser.setReadingStatus(formData.getFirst("readingStatus"));
        booksByUser.setRating(rating);
        booksByUserRepository.save(booksByUser);

        return new ModelAndView("redirect:/books/"+bookId);
    }
}
