package definitions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import definitions.support.TestState;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class CatalogDefinitions {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestState state;

    @Given("the catalog contains at least one book")
    public void theCatalogContainsAtLeastOneBook() throws Exception{
        executeGet(get("/api/catalog/books")
                .param("page", "0")
                .param("size", "1"));

        assertEquals(200, state.status, state.responseBody);

        JsonNode content = responseJson().path("content");
        assertTrue("The test database must contain at least one book",
                content.isArray() && !content.isEmpty());

        JsonNode firstBook = content.get(0);
        state.catalogBookId = firstBook.path("bookId").asText();
        state.catalogBookTitle = firstBook.path("title").asText();

        assertFalse(state.catalogBookId.isBlank());
        assertFalse(state.catalogBookTitle.isBlank());
    }

    private void executeGet(MockHttpServletRequestBuilder request) throws Exception {
        MvcResult result = mockMvc.perform(request
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        state.status = result.getResponse().getStatus();
        state.responseBody = result.getResponse().getContentAsString();
    }

    private JsonNode responseJson() throws Exception {
        return objectMapper.readTree(state.responseBody);
    }

    @When("I request the first page of the catalog")
    public void iRequestTheFirstPageOfTheCatalog() throws Exception{
        executeGet(get("/api/catalog/books")
                .param("page", "0")
                .param("size", "20")
                .param("sortBy", "title")
                .param("sortOrder", "asc"));
    }

    @Then("the catalog response should be successful and paginated")
    public void theCatalogResponseShouldBeSuccessfulAndPaginated() throws Exception{
        assertEquals(200, state.status, state.responseBody);

        JsonNode json = responseJson();
        assertTrue(json.path("content").isArray());
        assertTrue(json.hasNonNull("page"));
        assertTrue(json.hasNonNull("size"));
        assertTrue(json.hasNonNull("totalElements"));
        assertTrue(json.hasNonNull("totalPages"));
        assertTrue(json.hasNonNull("last"));
    }

    @And("the first catalog book should contain required fields")
    public void theFirstCatalogBookShouldContainRequiredFields() throws Exception{
        JsonNode firstBook = responseJson().path("content").get(0);

        String[] requiredFields = {
                "bookId", "isbn", "title", "author", "genre",
                "publicationYear", "description", "totalCopies",
                "availableCopies", "status"
        };

        for (String field : requiredFields) {
            assertTrue("Missing catalog field: " + field,
                    firstBook.hasNonNull(field));
        }
    }

    @When("I search using the first catalog book title")
    public void iSearchUsingTheFirstCatalogBookTitle() throws Exception{
        executeGet(get("/api/catalog/books")
                .param("query", state.catalogBookTitle)
                .param("page", "0")
                .param("size", "20"));
    }

    @And("the results should include the first catalog book")
    public void theResultsShouldIncludeTheFirstCatalogBook() throws Exception{
        boolean found = false;

        for (JsonNode book : responseJson().path("content")) {
            if (state.catalogBookId.equals(book.path("bookId").asText())) {
                found = true;
                break;
            }
        }

        assertTrue("Search results did not include the expected book", found);
    }

    @When("I request the catalog sorted by {string} in {string} order with page size {int}")
    public void iRequestTheCatalogSortedByInOrderWithPageSize(String arg0, String arg1, int arg2) throws Exception{
        executeGet(get("/api/catalog/books")
                .param("page", "0")
                .param("size", String.valueOf(arg2))
                .param("sortBy", arg0)
                .param("size", arg1)
                );
    }

    @And("the catalog page should be {int} with size {int}")
    public void theCatalogPageShouldBeWithSize(int arg0, int arg1) throws Exception{
        JsonNode json = responseJson();
        assertEquals(arg0, json.path("page").asInt());
        assertEquals(arg1, json.path("size").asInt());
    }

    @When("I search for a title that does not exist")
    public void iSearchForATitleThatDoesNotExist() throws Exception{
        executeGet(get("/api/catalog/books")
                .param("query", "NOT A BOOK TITLE")
                .param("page", "0")
                .param("size", "20"));
    }

    @And("the catalog content should be empty")
    public void theCatalogContentShouldBeEmpty() throws Exception{
        JsonNode json = responseJson();
        assertEquals(0, json.path("totalElements").asInt());
    }

    @When("I request details for the first catalog book")
    public void iRequestDetailsForTheFirstCatalogBook() throws Exception{
        executeGet(get("/api/catalog/books/" + state.catalogBookId));
    }

    @Then("the book details response should be {int}")
    public void theBookDetailsResponseShouldBe(int arg0) {
        assertEquals(arg0, state.status, state.responseBody);
    }

    @And("the book details should contain complete information")
    public void theBookDetailsShouldContainCompleteInformation() throws Exception{
        JsonNode firstBook = responseJson();

        String[] requiredFields = {
                "bookId", "isbn", "title", "author", "genre",
                "publicationYear", "description", "publisher",
                "pageCount", "language", "totalCopies",
                "availableCopies", "status", "createdAt", "updatedAt"
        };

        for (String field : requiredFields) {
            assertTrue("Missing catalog field: " + field,
                    firstBook.hasNonNull(field));
        }
    }

    @When("I request book details for ID {string}")
    public void iRequestBookDetailsForID(String arg0) throws Exception{
        executeGet(get("/api/catalog/books/" + arg0));
    }

    @And("the error field should be {string}")
    public void theErrorFieldShouldBe(String arg0) throws Exception{
        assertEquals(arg0, responseJson().path("error").asText());
    }
}
