import com.fasterxml.jackson.databind.ObjectMapper;
import com.soccergame.dto.auth.LoginRequest;
import com.soccergame.dto.auth.RegisterRequest;
import com.soccergame.SoccerGameApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes básicos para autenticação: registro e login.
 */
@SpringBootTest(classes = SoccerGameApplication.class)
@AutoConfigureMockMvc
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerAndLoginFlow() throws Exception {
        String username = "testuser";
        String password = "testpass";

        RegisterRequest register = new RegisterRequest(username, password);

        // register
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered"));

        // login
        LoginRequest login = new LoginRequest(username, password);
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()));
    }

    @Test
    void registerDuplicateShouldFail() throws Exception {
        String username = "dupuser";
        String password = "pass";

        RegisterRequest register = new RegisterRequest(username, password);

        // first register ok
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isOk());

        // second register should conflict (409) or bad request depending on implementation
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().is4xxClientError());
    }
}
