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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Map;
import java.util.UUID;

public class AuthenticationDefinitions {
    @Autowired
    private TestState state;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Given("I have valid details for a new user")
    public void iHaveValidDetailsForANewUser() {
        state.email = "user-" + UUID.randomUUID() + "@example.com";
        state.password = "SecurePass123!";
    }

    @When("I register the user")
    public void iRegisterTheUser() throws  Exception{
        // Write code here that turns the phrase above into concrete actions
        Map<String, String> request = Map.of(
                "email", state.email,
                "password", state.password,
                "firstName", "Test",
                "lastName", "User",
                "phoneNumber", "5137200058",
                "role", "LIBRARIAN"
        );

        MvcResult result = mockMvc.perform(
                post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andReturn();
        saveResponse(result);
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int arg0) {
        assertEquals(arg0, state.status);
    }

    @And("the response field {string} should match the registered email")
    public void theResponseFieldShouldMatchTheRegisteredEmail(String arg0) {
        assertEquals(jsonValue(arg0).asText(), state.email);
    }

    @And("the response field {string} should be {string}")
    public void theResponseFieldShouldBe(String arg0, String arg1) {
        assertEquals(jsonValue(arg0).asText(), arg1);
    }

    private JsonNode jsonValue(String field) {
        try {
            JsonNode node = objectMapper.readTree(state.responseBody);
            for (String part : field.split("\\.")) {
                node = node.path(part);
            }
            return node;
        } catch (Exception exception) {
            throw new AssertionError("Invalid JSON response: " + state.responseBody, exception);
        }
    }

    private void saveResponse(MvcResult result) throws Exception {
        state.status = result.getResponse().getStatus();
        state.responseBody = result.getResponse().getContentAsString();
    }

    @Given("a user has already registered")
    public void aUserHasAlreadyRegistered() throws Exception{
    }

    @When("I register the same user again")
    public void iRegisterTheSameUserAgain() throws Exception{
        iRegisterTheUser();
    }

    @When("I change the password to {string}")
    public void iChangeThePasswordTo(String arg0) {
        state.password = arg0;
    }

    @Given("a registered user exists")
    public void aRegisteredUserExists() throws Exception{
        state.email = "test@test1.com";
        state.password = "SecurePass123!";
        iRegisterTheUser();
    }

    @When("I log in with the correct password")
    public void iLogInWithTheCorrectPassword() throws Exception{
        loginWithPassword();
    }

    @And("the access token should be present")
    public void theAccessTokenShouldBePresent() {
        String token = jsonValue("accessToken").asText();
        assertTrue(token != null && !token.isBlank());
    }

    @And("the response field {string} should be {int}")
    public void theResponseFieldShouldBe(String arg0, int arg1) {
        assertEquals(jsonValue(arg0).asInt(), arg1, state.responseBody);
    }

    private void loginWithPassword() throws Exception {
        Map<String, String> request = Map.of(
                "email", state.email,
                "password", state.password
        );

        MvcResult result = mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andReturn();

        saveResponse(result);
    }

    @When("I log in with an invalid password")
    public void iLogInWithAnInvalidPassword() throws Exception{
        state.password = "1";
        loginWithPassword();
    }

    @Given("I am logged in")
    public void iAmLoggedIn() throws Exception{
        aRegisteredUserExists();
        loginWithPassword();
        assertEquals(200, state.status);
        state.token = jsonValue("accessToken").asText();
    }

    @When("I request my profile")
    public void iRequestMyProfile() throws Exception{
        MvcResult result = mockMvc.perform(
                get("/api/users/profile")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + state.token)
        ).andReturn();

        saveResponse(result);
    }

    @And("the response field {string} should be present")
    public void theResponseFieldShouldBePresent(String arg0) {
        assertFalse(jsonValue(arg0).isMissingNode());
    }

    @When("I request my profile without a token")
    public void iRequestMyProfileWithoutAToken() throws Exception{
        MvcResult result = mockMvc.perform(
                get("/api/users/profile")
        ).andReturn();

        saveResponse(result);
    }


}
