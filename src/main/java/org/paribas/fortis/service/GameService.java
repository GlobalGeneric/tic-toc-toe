package org.paribas.fortis.service;

import org.paribas.fortis.model.Game;
import org.paribas.fortis.model.Player;

public interface GameService {
    Game createGame(Player player);
}
