package definitions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import definitions.support.TestState;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class TestHooks {

    @Autowired
    private TestState state;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Before("@authentication")
    public void cleanBeforeScenario() {
        state.clear();
    }

    @After("@authentication")
    public void cleanAfterScenario() {
        state.clear();
    }

    @Before("@reservations")
    public void setupResrvations() throws Exception{
        state.email = "patron@patron.com";
        state.password = "Password123!";
        Map<String, String> patronRegistration = new HashMap<>();
        patronRegistration.put("email", state.email);
        patronRegistration.put("password", state.password);
        patronRegistration.put("firstName", "Test");
        patronRegistration.put("lastName", "Patron");
        patronRegistration.put("phoneNumber", "5137200058");
        patronRegistration.put("role", "PATRON");

        execute(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patronRegistration)));

        JsonNode patronRegistrationJson = responseJson();

        state.patronId = patronRegistrationJson.path("userId").asText(null);

        Map<String, String> librarianRegistration = new HashMap<>();
        librarianRegistration.put("email", "librarian@example.com");
        librarianRegistration.put("password", state.password);
        librarianRegistration.put("firstName", "Test");
        librarianRegistration.put("lastName", "Librarian");
        librarianRegistration.put("phoneNumber", "5137200058");
        librarianRegistration.put("role", "LIBRARIAN");

        execute(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(librarianRegistration)));

        JsonNode librarianRegistrationJson = responseJson();

        state.librarianId = librarianRegistrationJson.path("userId").asText(null);
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
}