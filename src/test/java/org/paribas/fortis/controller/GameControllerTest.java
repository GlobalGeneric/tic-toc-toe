package org.paribas.fortis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.paribas.fortis.controller.dto.ConnectRequest;
import org.paribas.fortis.model.Game;
import org.paribas.fortis.model.Player;
import org.paribas.fortis.model.TicToe;
import org.paribas.fortis.repository.GameRepository;
import org.paribas.fortis.service.GameServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.paribas.fortis.model.GameStatus.IN_PROGRESS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GameController.class)
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

    @MockBean
    SimpMessagingTemplate SimpMessagingTemplate;

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

    @Test
    void connect_toGame_with_valid_gamId_should_return_connected_() throws Exception {

        Player player1 = new Player("player-1");
        Player player2 = new Player("player-2");
        Game createdGame = new Game("gameId", player1, player2, IN_PROGRESS, new int[3][3], TicToe.O);
        given(gameService.createGame(player1)).willReturn(createdGame);
        given(gameService.connectToGame(player2, "gameId")).willReturn(createdGame);

        ConnectRequest connectRequest = new ConnectRequest();
        connectRequest.setGameId(createdGame.getGameId());
        connectRequest.setPlayer(player2);

        mockMvc.perform(post("/game/connect", connectRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(connectRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.gameId", is(createdGame.getGameId())))
                .andExpect(jsonPath("$.result.player1.login", is(createdGame.getPlayer1().getLogin())))
                .andExpect(jsonPath("$.result.player2.login", is(createdGame.getPlayer2().getLogin())))
                .andExpect(jsonPath("$.result.status", is("IN_PROGRESS")))
                .andExpect(jsonPath("$.hasError", is(false)));
    }

    @Test
    void connect_toGame_with_Not_valid_gamId_should_return_BadRequest_() throws Exception {

        Player player1 = new Player("player-1");
        Player player2 = new Player("player-2");
        Game createdGame = new Game("gameId", player1, null, IN_PROGRESS, new int[3][3], TicToe.O);
        given(gameService.createGame(player1)).willReturn(createdGame);

        ConnectRequest connectRequest = new ConnectRequest();
        connectRequest.setGameId("NotValidGameId3");
        connectRequest.setPlayer(player2);

        mockMvc.perform(post("/game/connect", connectRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(connectRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hasError", is(false)));
    }
}
