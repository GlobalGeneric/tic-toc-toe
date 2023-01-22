package org.paribas.fortis.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.paribas.fortis.model.Game;
import org.paribas.fortis.model.GameStatus;
import org.paribas.fortis.model.Player;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @InjectMocks
    GameServiceImpl gameService;

    @Test
    void createGameShouldReturnGameObject(){
        Player player = new Player("player-1");
       Game createdGame= gameService.createGame(player);

       assertThat(createdGame.getPlayer1().getLogin().equals("player-1"));
       assertThat(createdGame.getStatus().equals(GameStatus.NEW));
    }
}
