package io.javabrains.betterreads.controller;

import io.javabrains.betterreads.model.book.Book;
import io.javabrains.betterreads.model.userbooks.UserBooks;
import io.javabrains.betterreads.model.userbooks.UserBooksPrimaryKey;
import io.javabrains.betterreads.repository.BookRepository;
import io.javabrains.betterreads.repository.UserBooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserBooksRepository userBooksRepository;

    private final String COVER_IMAGE_ROOT = "https://covers.openlibrary.org/b/id/";

    @GetMapping("/books/{bookId}")
    public String getBook(@PathVariable String bookId, Model model, @AuthenticationPrincipal OAuth2User principal){

        Optional<Book> bookOptional = bookRepository.findById(bookId);

        if(bookOptional.isPresent()){
            Book book = bookOptional.get();

            String coverImageUrl = "/images/no-image.png";
            if(book.getCoverIds()!=null && book.getCoverIds().size()>0){
                coverImageUrl = COVER_IMAGE_ROOT+book.getCoverIds().get(0)+"-L.jpg";
            }

            model.addAttribute("coverImage",coverImageUrl);
            model.addAttribute("book",book);

            // To get principal and check whether the user had logged in
            if(principal!=null && principal.getAttribute("login")!=null) {
                String userId = principal.getAttribute("login");
                model.addAttribute("loginId",userId);

                UserBooksPrimaryKey key = new UserBooksPrimaryKey();
                key.setBookId(bookId);
                key.setUserId(userId);

                // get the book details for the user by passing the key
                Optional<UserBooks> userBooks = userBooksRepository.findById(key);
                if(userBooks.isPresent()) {
                    model.addAttribute("userBooks",userBooks.get());
                }else {
                    model.addAttribute("userBooks",new UserBooks());
                }
            }

            return "book";
        }

        return "book-not-found";
    }
}
