package org.paribas.fortis.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.paribas.fortis.model.Game;
import org.paribas.fortis.model.GameStatus;
import org.paribas.fortis.model.Player;
import org.paribas.fortis.model.TicToe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class GameRepositoryTest {

    @InjectMocks
    GameRepository gameRepository;

    @Test
    void getInstance_should_return_repository() {
        int[][] board = new int[3][3];
        Game game1 = new Game("game-Id-1", new Player("player-1"), null, GameStatus.NEW, board, TicToe.O);
        Game game2 = new Game("game-Id-2", new Player("player-1"), null, GameStatus.NEW, board, TicToe.O);
        gameRepository.setGame(game1);
        gameRepository.setGame(game2);

        assertThat(gameRepository.getGames().containsKey("game-Id-1"));
        assertFalse(game1.equals(game2));

    }

    @Test
    void getGames_should_return_numberOfGames(){
        int[][] board = new int[3][3];
        Game game1 = new Game("game-Id-1", new Player("player-1"), null, GameStatus.NEW, board, TicToe.O);
        Game game2 = new Game("game-Id-2", new Player("player-1"), null, GameStatus.NEW, board, TicToe.O);
        gameRepository.setGame(game1);
        gameRepository.setGame(game2);

        assertTrue(gameRepository.getGames().size()==2);

    }
}
