package org.paribas.fortis.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.paribas.fortis.exception.InvalidGameException;
import org.paribas.fortis.exception.InvalidParamException;
import org.paribas.fortis.model.Game;
import org.paribas.fortis.model.GameStatus;
import org.paribas.fortis.model.Player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @InjectMocks
    GameServiceImpl gameService;

    @Test
    void createGameShouldReturnGameObject() {
        Player player = new Player("player-1");
        Game createdGame = gameService.createGame(player);

        assertThat(createdGame.getPlayer1().getLogin().equals("player-1"));
        assertThat(createdGame.getStatus().equals(GameStatus.NEW));
    }

    @Test
    void connectTo_game() throws InvalidParamException, InvalidGameException {
        Player player1 = new Player("player-1");
        Game createdGame = gameService.createGame(player1);

        Player player2 = new Player("player-2");
        gameService.connectToGame(player2, createdGame.getGameId());

        assertThat(createdGame.getPlayer1().getLogin().equals("player-1"));
        assertThat(createdGame.getStatus().equals(GameStatus.IN_PROGRESS));

    }

    @Test
    void throw_InvalidGameException_when_connectingTo_wrong_GameID() throws InvalidGameException {

        Player player1 = new Player("player-1");
        gameService.createGame(player1);
        Player player2 = new Player("player-2");
        try {
            gameService.connectToGame(player2, "wrongGameId");
            fail();
        } catch (InvalidParamException invalidParamException) {
            assertEquals("Game with provided id doesn't exist", invalidParamException.getMessage());
        }
    }

    @Test
    void throw_InvalidParamException_when_player2_is_notNull() throws InvalidParamException{

        Player player1 = new Player("player-1");
        Game createdGame = gameService.createGame(player1);
        Player player2 = new Player("player-2");
        createdGame.setPlayer2(player2);
        try {
            gameService.connectToGame(player2, createdGame.getGameId());
            fail();
        } catch (InvalidGameException invalidGameException) {
            assertEquals("Game is not valid anymore", invalidGameException.getMessage());
        }
    }

}
