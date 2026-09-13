package assembly.general.api.seed;

import assembly.general.api.entity.Book;
import assembly.general.api.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(2)
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
                "978-0-13-468599-1",
                "Clean Code",
                "Robert C. Martin",
                "Technology",
                "2008",
                "A handbook of agile software craftsmanship",
                "Prentice Hall",
                464,
                "English",
                5,
                2
        );

        Book book2 = new Book(
                "978-0-13-475759-9",
                "Refactoring",
                "Martin Fowler",
                "Technology",
                "2018",
                "Improving the design of existing code",
                "Addison-Wesley",
                448,
                "English",
                3,
                0
        );

        Book book3 = new Book(
                "978-0-201-63361-0",
                "Design Patterns",
                "Erich Gamma",
                "Technology",
                "1994",
                "Elements of reusable object-oriented software",
                "Addison-Wesley",
                395,
                "English",
                4,
                4
        );

        Book book4 = new Book(
                "978-0-596-52068-7",
                "Head First Java",
                "Kathy Sierra",
                "Technology",
                "2005",
                "A beginner-friendly introduction to Java programming",
                "O'Reilly Media",
                688,
                "English",
                6,
                5
        );

        Book book5 = new Book(
                "978-0-7432-7356-5",
                "The Great Gatsby",
                "F. Scott Fitzgerald",
                "Classic",
                "1925",
                "A classic novel about wealth, love, and the American dream",
                "Scribner",
                180,
                "English",
                7,
                7
        );

        bookRepository.saveAll(List.of(
                book1,
                book2,
                book3,
                book4,
                book5
        ));
    }
}
