package com.ssf.day16workshop.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.ssf.day16workshop.model.Game;

@Repository
public class GameRepo {

    @Autowired
    @Qualifier("redis")
    private RedisTemplate<String, Object> template;

    private static final String GAME_HASH_KEY = "game";

    public void saveGame(Game game) {
        String key = GAME_HASH_KEY + ":" + game.getGid();
        template.opsForHash().put(key, "gid", Integer.toString(game.getGid()));
        template.opsForHash().put(key, "name", game.getName().toString());
        template.opsForHash().put(key, "year", Integer.toString(game.getYear()));
        template.opsForHash().put(key, "ranking", Integer.toString(game.getRanking()));
        template.opsForHash().put(key, "usersRated", Integer.toString(game.getUsersRated()));
        template.opsForHash().put(key, "url", game.getUrl().toString());

    }

}