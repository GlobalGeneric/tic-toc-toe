package org.paribas.fortis.controller;

import lombok.extern.slf4j.Slf4j;
import org.paribas.fortis.model.Player;
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


    @PostMapping("/start")
    public ResponseEntity start(@RequestBody Player player) {
        log.info("start game request: {}", player);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
