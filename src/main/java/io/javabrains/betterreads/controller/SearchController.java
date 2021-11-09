package io.javabrains.betterreads.controller;

import io.javabrains.betterreads.model.search.SearchResult;
import io.javabrains.betterreads.model.search.SearchResultBook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SearchController {

    private final WebClient webClient;

    private final String COVER_IMAGE_ROOT = "https://covers.openlibrary.org/b/id/";

    /**
     * Constructor
     * @param webClientBuilder
     */
    public SearchController(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder
                            .exchangeStrategies(ExchangeStrategies.builder()        // Added for handling DataBufferLimitException
                            .codecs(configurer -> configurer.defaultCodecs()
                                    .maxInMemorySize(16*1024*1024))
                            .build())
                            .baseUrl("https://openlibrary.org/search.json")
                            .build();
    }

    /**
     * Method to get search results of the book by searching for a string
     * @param query
     * @param model
     * @return
     */
    @GetMapping("/search")
    public String getSearchResults(@RequestParam String query, Model model){

        Mono<SearchResult> resultMono =
                this.webClient.get()
                    .uri("?q={query}", query)
                    .retrieve()
                    .bodyToMono(SearchResult.class);

        SearchResult result = resultMono.block();
        //model.addAttribute("searchResult",result);

        List<SearchResultBook> books = result.getDocs()
                                            .stream()
                                            .limit(20)      // limiting to only 10 records
                                            .map(bookResult -> getSearchResultBook(bookResult))
                                            .collect(Collectors.toList());

        model.addAttribute("searchResults",books);

        return "search";
    }

    /**
     * Search for the books with title
     * @param query
     * @param model
     * @return
     */
    @GetMapping("/search/book")
    public String getBookResults(@RequestParam String query, Model model){

        Mono<SearchResult> resultMono =
                this.webClient.get()
                        .uri("?title={query}", query)
                        .retrieve()
                        .bodyToMono(SearchResult.class);

        SearchResult result = resultMono.block();

        List<SearchResultBook> books = result.getDocs()
                .stream()
                .map(bookResult -> getSearchResultBook(bookResult))
                .collect(Collectors.toList());

        model.addAttribute("searchResults",books);

        return "search";
    }

    private SearchResultBook getSearchResultBook(SearchResultBook bookResult) {

        bookResult.setKey(bookResult.getKey().replace("/works/", ""));

        String coverId = bookResult.getCover_i();
        if (StringUtils.hasText(coverId)) {
            coverId = COVER_IMAGE_ROOT + coverId + "-M.jpg";
        } else {
            coverId = "/images/no-image.png";
        }
        bookResult.setCover_i(coverId);

        return bookResult;
    }
}
