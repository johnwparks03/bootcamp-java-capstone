package assembly.general.api.controllers;

import assembly.general.api.dto.BookDetailDto;
import assembly.general.api.dto.BooksResponse;
import assembly.general.api.service.BookService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/api/catalog")
@Validated
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public ResponseEntity<BooksResponse> getBooks(
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size,
            @RequestParam(defaultValue = "title") @Pattern(
                    regexp = "title|author|publicationYear",
                    message = "sortBy must be title, author, or publicationYear"
            ) String sortBy,
            @RequestParam(defaultValue = "asc") @Pattern(
                    regexp = "asc|desc",
                    message = "sortDirection must be asc or desc"
            ) String sortOrder,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String isbn,
            @RequestParam(defaultValue = "false") Boolean availableOnly
    ){
        BooksResponse response = bookService.getBooks(
                page, size, sortBy, sortOrder, query, genre, isbn, availableOnly
        );

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity<BookDetailDto> getBookById(@PathVariable UUID bookId){
        return ResponseEntity.ok(bookService.getBookById(bookId));
    }
}
