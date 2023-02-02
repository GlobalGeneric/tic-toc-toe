package org.paribas.fortis.controller;

import lombok.extern.slf4j.Slf4j;
import org.paribas.fortis.controller.dto.ConnectRequest;
import org.paribas.fortis.exception.InvalidGameException;
import org.paribas.fortis.exception.InvalidParamException;
import org.paribas.fortis.exception.NotFoundException;
import org.paribas.fortis.model.Game;
import org.paribas.fortis.model.GamePlay;
import org.paribas.fortis.model.Player;
import org.paribas.fortis.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/game")
@CrossOrigin("http://localhost:3000")
public class GameController {

    @Autowired
    GameService gameService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/start")
    public ResponseEntity<Game> start(@RequestBody Player player) {
        log.info("start game request: {}", player);
        return ResponseEntity.ok(gameService.createGame(player));
    }

    @PostMapping("/connect")
    public ResponseEntity<ResponseModel> connect(@RequestBody ConnectRequest request) {
        log.info("connect request: {}", request);
        try {
            return new ResponseEntity<>(new ResponseModel(false, gameService.connectToGame(request.getPlayer(), request.getGameId())), HttpStatus.OK);
        } catch (InvalidParamException | InvalidGameException e) {
            return new ResponseEntity<>(new ResponseModel(true, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/gameplay")
    public ResponseEntity<ResponseModel> gamePlay(@RequestBody GamePlay request) {
        log.info("gameplay: {}", request);
        try {
            Game game = gameService.gamePlay(request);
            simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
            return new ResponseEntity<>(new ResponseModel(false, game), HttpStatus.OK);
        } catch (NotFoundException | InvalidGameException e) {
            return new ResponseEntity<>(new ResponseModel(true, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/connect/random")
    public ResponseEntity<Game> connectRandom(@RequestBody Player player) throws NotFoundException {
        log.info("connect random {}", player);
        return ResponseEntity.ok(gameService.connectToRandomGame(player));
    }
}
