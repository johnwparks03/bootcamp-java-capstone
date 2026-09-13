package assembly.general.api.service;

import assembly.general.api.dto.BookDetailDto;
import assembly.general.api.dto.BookDto;
import assembly.general.api.dto.BooksResponse;
import assembly.general.api.entity.Book;
import assembly.general.api.exception.BookNotFoundException;
import assembly.general.api.repository.BookRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public BooksResponse getBooks(
            Integer page,
            Integer size,
            String sortBy,
            String sortOrder,
            String query,
            String genre,
            String isbn,
            Boolean availableOnly
    ){
        String sortField = switch (sortBy) {
            case "title" -> "title";
            case "author" -> "author";
            case "publicationYear" -> "publicationYear";
            default -> throw new IllegalArgumentException(
                    "sortBy must be title, author, or publicationYear"
            );
        };

        Sort.Direction direction = switch (sortOrder) {
            case "asc" -> Sort.Direction.ASC;
            case "desc" -> Sort.Direction.DESC;
            default -> throw new IllegalArgumentException(
                    "sortOrder must be asc or desc"
            );
        };

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(direction, sortField)
        );

        Specification<Book> specification = matches(query, genre, isbn, availableOnly);

        Page<Book> booksPage = bookRepository.findAll(specification, pageable);

        List<BookDto> books = booksPage.stream().map(this::convertBookToBookDto).toList();

        return new BooksResponse(
                books,
                booksPage.getNumber(),
                booksPage.getSize(),
                booksPage.getTotalElements(),
                booksPage.getTotalPages(),
                booksPage.isLast()
        );
    }

    private Specification<Book> matches(
            String query, String genre, String isbn, Boolean availableOnly
    ){
        return (root, query1, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (query != null && !query.isBlank()) {
                String searchTerm = "%" + query.trim().toLowerCase(Locale.ROOT) + "%";

                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("title")),
                                searchTerm
                        ),
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("author")),
                                searchTerm
                        )
                ));
            }

            if (genre != null && !genre.isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(root.get("genre"), genre)
                );
            }

            if (isbn != null && !isbn.isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(root.get("isbn"), isbn.trim())
                );
            }

            if (Boolean.TRUE.equals(availableOnly)) {
                predicates.add(
                        criteriaBuilder.greaterThan(
                                root.get("availableCopies"),
                                0
                        )
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private BookDto convertBookToBookDto(Book book){
        return new BookDto(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getPublicationYear(),
                book.getDescription(),
                book.getTotalCopies(),
                book.getAvailableCopies()
        );
    }
}
