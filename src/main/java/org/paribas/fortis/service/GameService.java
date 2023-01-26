package org.paribas.fortis.service;

import org.paribas.fortis.exception.InvalidGameException;
import org.paribas.fortis.exception.InvalidParamException;
import org.paribas.fortis.exception.NotFoundException;
import org.paribas.fortis.model.Game;
import org.paribas.fortis.model.GamePlay;
import org.paribas.fortis.model.Player;

public interface GameService {
    Game createGame(Player player);
    Game connectToGame(Player player2, String gameId) throws InvalidParamException, InvalidGameException;
    Game gamePlay(GamePlay gamePlay) throws NotFoundException, InvalidGameException;

}
