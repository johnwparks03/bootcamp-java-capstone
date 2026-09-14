package assembly.general.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class BookDetailDto {
    private UUID bookId;
    private String isbn;
    private String title;
    private String author;
    private String genre;
    private String publicationYear;
    private String description;
    private String publisher;
    private Integer pageCount;
    private String language;
    private Integer totalCopies;
    private Integer availableCopies;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;

    public BookDetailDto(
            UUID bookId,
            String isbn,
            String title,
            String author,
            String genre,
            String publicationYear,
            String description,
            String publisher,
            Integer pageCount,
            String language,
            Integer totalCopies,
            Integer availableCopies,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publicationYear = publicationYear;
        this.description = description;
        this.publisher = publisher;
        this.pageCount = pageCount;
        this.language = language;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.status = (availableCopies > 0) ? "AVAILABLE" : "CHECKED_OUT";
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
