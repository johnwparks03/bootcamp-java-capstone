package assembly.general.api.seed;

import assembly.general.api.entity.Book;
import assembly.general.api.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BookDataSeeder implements CommandLineRunner {

    private final BookRepository bookRepository;

    @Autowired
    public BookDataSeeder(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        bookRepository.deleteAll();

        Book book1 = new Book(
                "test_isbn",
                "This is my title",
                "Test Author",
                "Test Genre",
                "2026",
                "This is my long description of this book",
                "the best publisher",
                365,
                "English",
                10,
                9
        );

        bookRepository.save(book1);
    }
}
