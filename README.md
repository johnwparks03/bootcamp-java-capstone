# Digital Library Management System API

---

## User Story Compliance

See [User Stories](instructionDocs/user-stories.md) for user story information

- **US-001**: Complete
- **US-002**: Complete
- **US-003**: Complete
- **US-004**: Complete
- **US-005**: Complete
- **US-006**: Complete
- **US-007**: Complete
- **US-008**: Complete
- **US-009**: Complete
- **US-010**: Complete
- **US-011**: Complete

**All 11 user stories have been implemented and acceptance criteria met**

---

## Implemented API Endpoints

### Authentication & User Management (3)

- `POST /api/auth/register` - Create new user account
- `POST /api/auth/login` - Authenticate and receive JWT token
- `GET /api/users/profile` - View user profile with statistics (Authentication required)

### Catalog Management (2)

- `GET /api/catalog/books` - Browse and search books with pagination
- `GET /api/catalog/books/{bookId}` - View detailed book information

### Reservation Management (5)

- `POST /api/reservations` - Reserve an available book (Authentication required)
- `GET /api/reservations` - View active reservations (Authentication required)
- `POST /api/reservations/{reservationId}/checkout` - Checkout book (Librarian only) (Authentication required)
- `POST /api/reservations/{reservationId}/return` - Return book with late fee calculation (Librarian only) (Authentication required)
- `GET /api/reservations/history` - View complete borrowing history (Authentication required)

See **[API documentation](docs/api-endpoints.md)** for further documentation

Also see Swagger documentation for an interactive view of available endpoints

- **Local Swagger UI:** `http://localhost:8080/swagger-ui/index.html`
- **Production Swagger UI:** `<deployed-application-url>/swagger-ui/index.html`

---

## Technical Quality

- Achieved **85%** test coverage
- All endpoints are tested with Cucumber unit tests
- Proper error handling (400, 401, 403, 404, 500) implemented through [GlobalExceptionHandler](src/main/java/assembly/general/api/exception/GlobalExceptionHandler.java)
- Security is properly implemented
  - Authenticated routes require JWT authentication
  - Role-based access routes require LIBRARIAN role

---

## Functional Verification
- The complete reservation lifecycle (reserve -> checkout -> return) works and is tested
- Role-based access control is enforced on routes requiring LIBRARIAN role
- Real-time book availability tracking is working and tested
- Late fee calculation is accurate and tested
- Pagination and search is functional and allows users to search for books