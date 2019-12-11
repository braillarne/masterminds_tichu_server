/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.acrm.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rocks.process.acrm.business.service.GameBusinessService;
import rocks.process.acrm.data.domain.Game;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class LobbyEndpoint {
    @Autowired
    private GameBusinessService gameBusinessService;

    @GetMapping(path = "/lobby", produces = "application/json")
    public List<Game> getAllGames(){
        return gameBusinessService.getAllGames();
    }
}
