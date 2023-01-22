package org.paribas.fortis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.paribas.fortis.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class GameControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void playGame() throws Exception {

        Player player = new Player("player-1");
        mockMvc.perform(MockMvcRequestBuilders.post("/game/start")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(player))
        ).andExpect(status().isCreated());
    }
}
