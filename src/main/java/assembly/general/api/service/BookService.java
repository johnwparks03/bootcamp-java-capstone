package assembly.general.api.service;

import assembly.general.api.dto.BookDetailDto;
import assembly.general.api.entity.Book;
import assembly.general.api.exception.BookNotFoundException;
import assembly.general.api.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    public BookDetailDto getBookById(UUID bookId){
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));

        return new BookDetailDto(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getPublicationYear(),
                book.getDescription(),
                book.getPublisher(),
                book.getPageCount(),
                book.getLanguage(),
                book.getTotalCopies(),
                book.getAvailableCopies(),
                book.getCreatedAt(),
                book.getUpdatedAt()
        );
    }
}
