package definitions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import definitions.support.TestState;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class ReservationDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestState state;

    @Given("a patron is registered and logged in")
    public void aPatronIsRegisteredAndLoggedIn() throws Exception {
        state.email = "patron@patron.com";
        state.password = "Password123!";
        state.token = login(state.email, state.password);
    }

    private String login(String email, String password)
            throws Exception {

        Map<String, String> login = Map.of(
                "email", email,
                "password", password
        );

        execute(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)));

        assertEquals(200, state.status, state.responseBody);
        return responseJson().path("accessToken").asText();
    }

    private void execute(
            org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder request)
            throws Exception {
        MvcResult result = mockMvc.perform(request).andReturn();
        state.status = result.getResponse().getStatus();
        state.responseBody = result.getResponse().getContentAsString();
    }

    private JsonNode responseJson() throws Exception {
        return objectMapper.readTree(state.responseBody);
    }

    @And("the catalog contains an available book")
    public void theCatalogContainsAnAvailableBook() throws Exception{
        JsonNode books = getAllBooks();
        JsonNode book = getOneBook(books, true);

        state.catalogBookId = book.path("bookId").asText();
        state.catalogBookTitle = book.path("title").asText();

        assertFalse(state.catalogBookId.isBlank());
    }

    private JsonNode getAllBooks() throws Exception {
        execute(get("/api/catalog/books")
                .param("page", "0")
                .param("size", "100"));

        assertEquals(200, state.status, state.responseBody);
        return responseJson().path("content");
    }

    private JsonNode getOneBook(JsonNode books, boolean available) {
        for (JsonNode book : books) {
            boolean hasCopies = book.path("availableCopies").asInt() > 0;
            if (hasCopies == available) {
                return book;
            }
        }

        fail("No matching book found for available=" + available);
        return null;
    }

    @When("the patron reserves the available book")
    public void thePatronReservesTheAvailableBook() throws Exception{
        reserve(state.token, state.catalogBookId);
        state.reservationId = responseJson().path("reservationId").asText();
    }

    private void reserve(String token, String bookId) throws Exception {
        Map<String, String> body = Map.of("bookId", bookId);
        var request = post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body));

        if (token != null) {
            request.header("Authorization", "Bearer " + token);
        }

        execute(request);
    }

    @Then("the reservation response should be {int}")
    public void theReservationResponseShouldBe(int arg0) {
        assertEquals(arg0, state.status, state.responseBody);
    }

    @And("the reservation status should be {string}")
    public void theReservationStatusShouldBe(String arg0) throws Exception{
        assertEquals(arg0, responseJson().path("status").asText());
    }

    @And("the reservation should contain the book ID")
    public void theReservationShouldContainTheBookID() throws Exception{
        assertEquals(state.catalogBookId, responseJson().path("bookId").asText());
    }

    @And("the reservation should contain the patron ID")
    public void theReservationShouldContainThePatronID() throws Exception{
        assertEquals(state.patronId, responseJson().path("userId").asText());
    }

    @And("the reservation expiration should be {int} days after reservation")
    public void theReservationExpirationShouldBeDaysAfterReservation(int arg0) throws Exception{
        int daysBetween = Math.toIntExact(ChronoUnit.DAYS.between(
                LocalDateTime.parse(responseJson().path("reservedAt").asText()),
                LocalDateTime.parse(responseJson().path("expiresAt").asText())
        ));
        assertEquals(arg0, daysBetween);
    }

    @And("the response message should be {string}")
    public void theResponseMessageShouldBe(String arg0) throws Exception{
        assertEquals(arg0, responseJson().path("message").asText());
    }

    @Given("the patron has an active reservation")
    public void thePatronHasAnActiveReservation() throws Exception{
        thePatronReservesTheAvailableBook();
        assertEquals(201, state.status, state.responseBody);
    }

    @When("the patron requests active reservations")
    public void thePatronRequestsActiveReservations() throws Exception{
        execute(get("/api/reservations")
                .header("Authorization", "Bearer " + state.token));
    }

    @And("the active reservations response should contain the reservation")
    public void theActiveReservationsResponseShouldContainTheReservation() throws Exception{
        JsonNode reservations = responseJson().path("reservations");
        assertTrue(reservations.isArray());

        boolean found = false;
        for (JsonNode reservation : reservations) {
            if (state.reservationId.equals(reservation.path("reservationId").asText())) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Reservation was not returned as active");
    }

    @And("the active reservation should include book details")
    public void theActiveReservationShouldIncludeBookDetails() throws Exception{
        JsonNode reservation = findReservation(responseJson().path("reservations"));
        assertFalse(reservation.path("bookTitle").asText().isBlank());
        assertFalse(reservation.path("bookAuthor").asText().isBlank());
    }

    @And("the active reservation status should be {string}")
    public void theActiveReservationStatusShouldBe(String arg0) throws Exception{
        JsonNode reservation = findReservation(responseJson().path("reservations"));
        assertEquals(arg0, reservation.path("status").asText());
    }

    private JsonNode findReservation(JsonNode reservations) {
        for (JsonNode reservation : reservations) {
            if (state.reservationId.equals(
                    reservation.path("reservationId").asText())) {
                return reservation;
            }
        }
        fail("Reservation was not found");
        return null;
    }

    @When("an unauthenticated user reserves the available book")
    public void anUnauthenticatedUserReservesTheAvailableBook() throws Exception{
        reserve(null, state.catalogBookId);
    }

    @Given("the available book has no available copies")
    public void theAvailableBookHasNoAvailableCopies() throws Exception{
        JsonNode book = getOneBook(getAllBooks(), false);
        state.catalogBookId = book.path("bookId").asText();
        assertEquals(0, book.path("availableCopies").asInt());
    }

    @And("a librarian is logged in")
    public void aLibrarianIsLoggedIn() throws Exception{
        // Write code here that turns the phrase above into concrete actions
        state.librarianToken = login("librarian@example.com", state.password);
    }

    @When("the librarian checks out the reservation")
    public void theLibrarianChecksOutTheReservation() throws Exception{
        Map<String, String> body = Map.of("notes", "Automated test checkout");

        execute(post("/api/reservations/{reservationId}/checkout",
                state.reservationId)
                .header("Authorization", "Bearer " + state.librarianToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)));
    }

    @And("the checkout response should contain a checked-out timestamp")
    public void theCheckoutResponseShouldContainACheckedOutTimestamp() throws Exception{
        assertTrue(responseJson().hasNonNull("checkedOutAt"));
    }

    @And("the due date should be {int} days after checkout")
    public void theDueDateShouldBeDaysAfterCheckout(int arg0) throws Exception{
        int daysBetween = Math.toIntExact(ChronoUnit.DAYS.between(
                LocalDateTime.parse(responseJson().path("checkedOutAt").asText()),
                LocalDateTime.parse(responseJson().path("dueDate").asText())
        ));
        assertEquals(arg0, daysBetween);
    }

    @And("the response message should contain {string}")
    public void theResponseMessageShouldContain(String arg0) throws Exception{
        assertTrue(responseJson().path("message").asText().contains(arg0));
    }

    @When("the patron checks out the reservation")
    public void thePatronChecksOutTheReservation() throws Exception{
        Map<String, String> body = Map.of("notes", "Automated test checkout");

        execute(post("/api/reservations/{reservationId}/checkout",
                state.reservationId)
                .header("Authorization", "Bearer " + state.librarianToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)));
    }

    @Given("the patron has a checked-out reservation")
    public void thePatronHasACheckedOutReservation() throws Exception{
        thePatronHasAnActiveReservation();
        aLibrarianIsLoggedIn();
        theLibrarianChecksOutTheReservation();
        assertEquals(200, state.status, state.responseBody);
    }

    @When("the librarian returns the reservation with condition {string}")
    public void theLibrarianReturnsTheReservationWithCondition(String arg0) throws Exception{
        Map<String, String> body = new HashMap<>();
        body.put("condition", arg0);
        body.put("notes", "Automated test return");

        execute(post("/api/reservations/{reservationId}/return",
                state.reservationId)
                .header("Authorization", "Bearer " + state.librarianToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)));
    }

    @When("the patron returns the reservation with condition {string}")
    public void thePatronReturnsTheReservationWithCondition(String arg0) throws Exception{
        Map<String, String> body = new HashMap<>();
        body.put("condition", arg0);
        body.put("notes", "Automated test return");

        execute(post("/api/reservations/{reservationId}/return",
                state.reservationId)
                .header("Authorization", "Bearer " + state.token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)));
    }

    @Given("the patron already has {int} active reservations")
    public void thePatronAlreadyHasActiveReservations(int arg0) {
    }

    @When("an unauthenticated user requests borrowing history")
    public void anUnauthenticatedUserRequestsBorrowingHistory() throws Exception{
        execute(get("/api/reservations/history")
                .param("page", "0")
                .param("size", "20"));
    }

    @When("the patron requests borrowing history")
    public void thePatronRequestsBorrowingHistory() throws Exception{
        execute(get("/api/reservations/history")
                .header("Authorization", "Bearer " + state.token)
                .param("page", "0")
                .param("size", "20"));
    }

    @And("the borrowing history response should be paginated")
    public void theBorrowingHistoryResponseShouldBePaginated() throws Exception{
        JsonNode json = responseJson();
        assertTrue(json.path("content").isArray());
        assertTrue(json.hasNonNull("page"));
        assertTrue(json.hasNonNull("size"));
        assertTrue(json.hasNonNull("totalElements"));
        assertTrue(json.hasNonNull("totalPages"));
        assertTrue(json.hasNonNull("last"));
    }

    @And("the history should include reservation details")
    public void theHistoryShouldIncludeReservationDetails() throws Exception{
        assertTrue(responseJson().path("content").size() > 0);
        JsonNode item = responseJson().path("content").get(0);
        assertTrue(item.hasNonNull("reservationId"));
        assertTrue(item.hasNonNull("reservedAt"));
        assertTrue(item.hasNonNull("status"));
    }


    @And("the history should include a {string} field")
    public void theHistoryShouldIncludeAField(String arg0) throws Exception{
        assertTrue(responseJson().path("content").get(0).has(arg0));
    }

}
