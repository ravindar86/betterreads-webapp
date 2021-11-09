package io.javabrains.betterreads.controller;

import io.javabrains.betterreads.model.authorbooks.AuthorBooks;
import io.javabrains.betterreads.repository.AuthorBooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;
import java.util.List;

@Controller
public class AuthorController {

    @Autowired
    private AuthorBooksRepository authorBooksRepository;

    private final String COVER_IMAGE_ROOT = "https://covers.openlibrary.org/b/id/";

    @GetMapping("/authors/{authorNames}")
    public String getAuthor(@PathVariable String authorNames, Model model, @AuthenticationPrincipal OAuth2User principal) {
        System.out.println(authorNames);
      //  List<AuthorBooks> authorBooksList =  authorBooksRepository.findAllById(Arrays.asList(authorId));
        List<AuthorBooks> authorBooksList =  authorBooksRepository.findAllByName(authorNames);
        authorBooksList.forEach(authorBooks -> {
            String coverImageUrl = "/images/no-image.png";
            if(authorBooks.getCoverIds()!=null && authorBooks.getCoverIds().size()>0){
                coverImageUrl = COVER_IMAGE_ROOT+authorBooks.getCoverIds().get(0)+"-M.jpg";
            }
            authorBooks.setCoverUrl(coverImageUrl);
        });
        model.addAttribute("authorBooks",authorBooksList);
       // return authorBooksList.get(0).getBookNames().toString();
         return "author";
    }
}
