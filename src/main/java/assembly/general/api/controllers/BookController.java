package assembly.general.api.controllers;

import assembly.general.api.dto.BookDetailDto;
import assembly.general.api.dto.BooksResponse;
import assembly.general.api.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/api/catalog")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

//    @GetMapping("/books")
//    public ResponseEntity<BooksResponse> searchBooks(){
//
//    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity<BookDetailDto> getBookById(@PathVariable UUID bookId){
        return ResponseEntity.ok(bookService.getBookById(bookId));
    }
}
