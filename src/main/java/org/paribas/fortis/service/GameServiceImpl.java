package org.paribas.fortis.service;

import org.paribas.fortis.model.Game;
import org.paribas.fortis.model.Player;
import org.paribas.fortis.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.paribas.fortis.model.GameStatus.NEW;

@Service
public class GameServiceImpl implements GameService{

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
}
