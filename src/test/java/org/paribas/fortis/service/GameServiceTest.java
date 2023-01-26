package org.paribas.fortis.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.paribas.fortis.exception.InvalidGameException;
import org.paribas.fortis.exception.InvalidParamException;
import org.paribas.fortis.exception.NotFoundException;
import org.paribas.fortis.model.*;

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
    void throw_InvalidParamException_when_player2_is_notNull() throws InvalidParamException {

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

    @Test
    void throw_NotFoundException_when_gameId_notValid() throws InvalidGameException {
        GamePlay gamePlay = new GamePlay(TicToe.O, 0, 0, "gameId");
        try {
            gameService.gamePlay(gamePlay);
        } catch (NotFoundException e) {
            assertEquals("Game not found", e.getMessage());
        }
    }

    @Test
    void throw_InvalidGameException_when_game_status_isFinished() throws InvalidGameException, InvalidParamException, NotFoundException {
        Player player1 = new Player("player-1");
        Game createdGame = gameService.createGame(player1);
        Player player2 = new Player("player-2");
        gameService.connectToGame(player2, createdGame.getGameId());
        createdGame.setStatus(GameStatus.FINISHED);
        GamePlay gamePlay = new GamePlay(TicToe.O, 0, 0, createdGame.getGameId());
        try {
            gameService.gamePlay(gamePlay);
        } catch (InvalidGameException e) {
            assertEquals("Game is already finished", e.getMessage());
        }
    }

    @Test
    void gamePlay() throws InvalidParamException, InvalidGameException, NotFoundException {
        Player player1 = new Player("player-1");
        Game createdGame = gameService.createGame(player1);
        Player player2 = new Player("player-2");
        gameService.connectToGame(player2, createdGame.getGameId());

        GamePlay gamePlay = new GamePlay(TicToe.O, 0, 0, createdGame.getGameId());
        Game game2 = gameService.gamePlay(gamePlay);

        assertThat(game2.getPlayer1().getLogin().equals("player-1"));
        assertThat(game2.getStatus().equals(GameStatus.IN_PROGRESS));
    }

    @Test
    void gamePlay_checkWinner() throws InvalidParamException, InvalidGameException, NotFoundException {
        Player player1 = new Player("player-1");
        Game createdGame = gameService.createGame(player1);
        Player player2 = new Player("player-2");
        gameService.connectToGame(player2, createdGame.getGameId());

        GamePlay game_Play1_o = new GamePlay(TicToe.O, 0, 0, createdGame.getGameId());
        gameService.gamePlay(game_Play1_o);

        GamePlay gamePlay1_x = new GamePlay(TicToe.X, 1, 0, createdGame.getGameId());
        gameService.gamePlay(gamePlay1_x);

        GamePlay game_Play2_o = new GamePlay(TicToe.O, 0, 1, createdGame.getGameId());
        gameService.gamePlay(game_Play2_o);

        GamePlay gamePlay2_x = new GamePlay(TicToe.X, 2, 0, createdGame.getGameId());
        gameService.gamePlay(gamePlay2_x);

        GamePlay game_Play3_o = new GamePlay(TicToe.O, 0, 2, createdGame.getGameId());
        Game gameResult = gameService.gamePlay(game_Play3_o);

        assertThat(gameResult.getWinner().getValue().equals(TicToe.O));
    }
}
