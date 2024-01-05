package com.ssf.day16workshop.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ssf.day16workshop.model.Game;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

@Service
public class GameService {

    public List<Game> readFile(String fileName) {
        List<Game> games = new ArrayList<>();

        try (InputStream is = new FileInputStream(new File(fileName))) {
            JsonReader jsonreader = Json.createReader(is);
            JsonArray jsonArray = jsonreader.readArray();

            for (JsonValue gameValue : jsonArray) {
                JsonObject gameObject = (JsonObject) gameValue;

                Game game = new Game();
                game.setGid(gameObject.getInt("gid"));
                game.setName(gameObject.getString("name"));
                game.setYear(gameObject.getInt("year"));
                game.setRanking(gameObject.getInt("ranking"));
                game.setUsersRated(gameObject.getInt("users_rated"));
                game.setUrl(gameObject.getString("image"));

                games.add(game);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return games;
    }
    
}