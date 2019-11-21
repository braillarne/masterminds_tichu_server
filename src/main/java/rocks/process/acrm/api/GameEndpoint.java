package rocks.process.acrm.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rocks.process.acrm.business.service.GameBusinessService;
import rocks.process.acrm.data.domain.Game;
import rocks.process.acrm.data.domain.NewGameHandler;
import rocks.process.acrm.data.domain.Profile;
import springfox.documentation.spring.web.json.Json;

import javax.validation.ConstraintViolationException;
import java.net.URI;

@RestController
@RequestMapping(path = "/api")
public class GameEndpoint {

    @Autowired
    private GameBusinessService gameBusinessService;

    @PostMapping(path = "/game", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Game> postGame(@RequestBody Game game) throws Exception {
        try {
            gameBusinessService.saveGame(game);
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getConstraintViolations().iterator().next().getMessage());
        }

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest().path("/{customerId}")
            .buildAndExpand(game.getId()).toUri();

        return ResponseEntity.created(location).body(game);
    }

    @PostMapping(path = "/game/new", consumes = "application/json", produces = "application/json")
    public Game createNewGame(@RequestBody NewGameHandler newGameHandler) throws Exception {
        Game newGame = null;
        try {
            newGame = gameBusinessService.createGame(newGameHandler.getProfileID(),newGameHandler.getGameName());
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getConstraintViolations().iterator().next().getMessage());
        }

        return newGame;
    }

}
