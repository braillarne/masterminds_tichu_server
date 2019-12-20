/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package masterminds.tichu.server.api;

import masterminds.tichu.server.business.service.GameBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import masterminds.tichu.server.data.domain.Game;

import java.util.List;

/**
 * Author(S): Nelson Braillard
 */
@RestController
@RequestMapping(path = "/api")
public class LobbyEndpoint {
    @Autowired
    private GameBusinessService gameBusinessService;

    @GetMapping(path = "/lobby", produces = "application/json")
    public List<Game> getAllGames(){
        return gameBusinessService.getAllGames();
    }

    @GetMapping(path = "/lobby/open", produces = "application/json")
    public List<Game> getAllOpenGames(){
        return gameBusinessService.getAllOpenGames();
    }
}
