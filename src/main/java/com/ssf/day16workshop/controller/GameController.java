package com.ssf.day16workshop.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssf.day16workshop.model.Game;
import com.ssf.day16workshop.repo.GameRepo;
import com.ssf.day16workshop.service.GameService;

@RestController
@RequestMapping
public class GameController {

    @Autowired
    GameService gameService;

    @Autowired
    GameRepo gameRepo;

    private static final String GAME_HASH_KEY = "game";

    @PostMapping("/api/boardgame")
    public ResponseEntity<Map<String, Object>> insertGame() {
        String fileName = "game.json";

        try {
            List<String> redisKeys = new ArrayList<>();
            List<Game> games = gameService.readFile(fileName);
            for (Game game : games) {
                String key = GAME_HASH_KEY + ":" + game.getGid();
                redisKeys.add(key);
                gameRepo.saveGame(game);
            }

            Map<String, Object> responsePayload = new HashMap<>();
            responsePayload.put("insert_count", games.size());
            responsePayload.put("id", redisKeys);

            return ResponseEntity.status(201).body(responsePayload);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(Collections.singletonMap("error",
                    "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/api/boardgame/{id}")
    public ResponseEntity<Map<String, Object>> getGame(@PathVariable String id) {
        try {
            Game game = gameRepo.getGame(id);

            if (game != null) {
                Map<String, Object> responsePayload = new HashMap<>();
                responsePayload.put("game", game);
                return ResponseEntity.ok(responsePayload);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "Board game not found"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Internal Server Error"));
        }
    }

}
