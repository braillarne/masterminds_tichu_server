/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.acrm.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rocks.process.acrm.business.service.GameBusinessService;
import rocks.process.acrm.business.service.ProfileService;
import rocks.process.acrm.data.domain.Game;
import rocks.process.acrm.data.domain.Profile;

import javax.validation.ConstraintViolationException;
import java.net.URI;
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
