package org.paribas.fortis.controller;

import lombok.extern.slf4j.Slf4j;
import org.paribas.fortis.controller.dto.ConnectRequest;
import org.paribas.fortis.exception.InvalidGameException;
import org.paribas.fortis.exception.InvalidParamException;
import org.paribas.fortis.model.Game;
import org.paribas.fortis.model.Player;
import org.paribas.fortis.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/game")
public class GameController {

    @Autowired
    GameService gameService;

    @PostMapping("/start")
    public ResponseEntity<Game> start(@RequestBody Player player) {
        log.info("start game request: {}", player);
        return ResponseEntity.ok(gameService.createGame(player));
    }

    @PostMapping("/connect")
    public ResponseEntity<ResponseModel> connect(@RequestBody ConnectRequest request) {
        log.info("connect request: {}", request);
        try {
            return new ResponseEntity<>(new ResponseModel(false,  gameService.connectToGame(request.getPlayer(), request.getGameId())), HttpStatus.OK);
        }catch (InvalidParamException | InvalidGameException e)
        {
            return new ResponseEntity<>(new ResponseModel(true, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
