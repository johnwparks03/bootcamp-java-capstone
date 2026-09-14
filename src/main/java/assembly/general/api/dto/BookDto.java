package assembly.general.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BookDto {
    private UUID bookId;
    private String isbn;
    private String title;
    private String author;
    private String genre;
    private String publicationYear;
    private String description;
    private Integer totalCopies;
    private Integer availableCopies;
    private String status;

    public BookDto(
            UUID bookId,
            String isbn,
            String title,
            String author,
            String genre,
            String publicationYear,
            String description,
            Integer totalCopies,
            Integer availableCopies
    ) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publicationYear = publicationYear;
        this.description = description;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.status = (availableCopies > 0) ? "AVAILABLE" : "CHECKED_OUT";
    }
}
