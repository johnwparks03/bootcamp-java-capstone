package assembly.general.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="books")
@Getter
@Setter
public class Book extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="book_id")
    private UUID id;

    @Column(name="isbn", nullable = false, unique = true)
    private String isbn;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="author", nullable = false)
    private String author;

    @Column(name="genre", nullable = false)
    private String genre;

    @Column(name="publication_year", nullable = false)
    private String publicationYear;

    @Column(name="description", nullable = false)
    private String description;

    @Column(name="publisher", nullable = false)
    private String publisher;

    @Column(name="page_count", nullable = false)
    private Integer pageCount;

    @Column(name="language", nullable = false)
    private String language;

    @Column(name="total_copies", nullable = false)
    private Integer totalCopies;

    @Column(name="available_copies", nullable = false)
    private Integer availableCopies;

    @Column(name="created_at")
    private Instant createdAt;

    protected Book(){}

    public Book(
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
            Integer availableCopies
    ){
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
        this.createdAt = Instant.now();
    }

    public void decrementAvailableCopies(){
        this.availableCopies = this.availableCopies - 1;
    }
}
