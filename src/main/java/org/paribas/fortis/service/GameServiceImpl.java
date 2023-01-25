package org.paribas.fortis.service;

import org.paribas.fortis.exception.InvalidGameException;
import org.paribas.fortis.exception.InvalidParamException;
import org.paribas.fortis.model.Game;
import org.paribas.fortis.model.Player;
import org.paribas.fortis.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.paribas.fortis.model.GameStatus.IN_PROGRESS;
import static org.paribas.fortis.model.GameStatus.NEW;

@Service
public class GameServiceImpl implements GameService {

    @Override
    public Game createGame(Player player) {
        Game game = new Game();
        game.setBoard(new int[3][3]);
        game.setGameId(UUID.randomUUID().toString());
        game.setPlayer1(player);
        game.setStatus(NEW);
        GameRepository.getInstance().setGame(game);
        return game;
    }

    @Override
    public Game connectToGame(Player player2, String gameId) throws InvalidParamException, InvalidGameException {
        if (!GameRepository.getInstance().getGames().containsKey(gameId)) {
            throw new InvalidParamException("Game with provided id doesn't exist");
        }
        Game game = GameRepository.getInstance().getGames().get(gameId);

        if (game.getPlayer2() != null) {
            throw new InvalidGameException("Game is not valid anymore");
        }
        game.setPlayer2(player2);
        game.setStatus(IN_PROGRESS);
        GameRepository.getInstance().setGame(game);
        return game;
    }
}
