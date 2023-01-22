package org.paribas.fortis.repository;

import org.paribas.fortis.model.Game;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class GameRepository {
    private static Map<String, Game> games;
    private static GameRepository instance;

    private GameRepository() {
        games = new HashMap<>();
    }

    public static synchronized GameRepository getInstance() {
        if (instance == null) {
            instance = new GameRepository();
        }
        return instance;
    }

    public Map<String, Game> getGames() {
        return games;
    }

    public void setGame(Game game) {
        games.put(game.getGameId(), game);
    }
}
