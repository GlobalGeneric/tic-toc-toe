package org.paribas.fortis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.paribas.fortis.model.Game;
import org.paribas.fortis.model.Player;
import org.paribas.fortis.repository.GameRepository;
import org.paribas.fortis.service.GameService;
import org.paribas.fortis.service.GameServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.paribas.fortis.model.GameStatus.NEW;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = GameController.class)
public class GameControllerTest {

    @Autowired
    MockMvc mockMvc;

    @InjectMocks
    private GameController gameController;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    GameServiceImpl gameService;

    @MockBean
    GameRepository gameRepository;

    @Test
    void post_palyer_object_should_return_Status_OK() throws Exception {

        Player player = new Player("player-1");
        mockMvc.perform(post("/game/start")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(player))
        ).andExpect(status().isOk());
    }

    @Test
    void player_withNull_login_thenReturns400() throws Exception {
        //player with null login
        Player player = new Player();
        mockMvc.perform(post("/game/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(player)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void post_palyer_object_should_store() throws Exception {
        Player player = new Player("player-1");
        mockMvc.perform(post("/game/start")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(player))
        ).andExpect(status().isOk());

        ArgumentCaptor<Player> argumentCaptor = ArgumentCaptor.forClass(Player.class);
        verify(gameService, times(1)).createGame(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getLogin()).isEqualTo("player-1");
    }
}
