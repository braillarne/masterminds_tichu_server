package rocks.process.acrm.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rocks.process.acrm.business.service.GameBusinessService;
import rocks.process.acrm.data.domain.*;

import javax.validation.ConstraintViolationException;
import java.net.URI;

@RestController
@RequestMapping(path = "/api")
public class GameEndpoint {

    @Autowired
    private GameBusinessService gameBusinessService;

    @GetMapping(path = "/game/{id}", produces = "application/json")
    public ResponseEntity<Game> getGame(@PathVariable(value = "gameID") String gameID) {
        Game game = null;
        try {
            game = gameBusinessService.getGame(Long.parseLong(gameID));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.ok(game);
    }

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
    public Game createNewGame(@RequestBody GameHandler GameHandler) throws Exception {
        Game newGame = null;
        try {
            newGame = gameBusinessService.createGame(GameHandler.getProfileID(),GameHandler.getGameName());
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getConstraintViolations().iterator().next().getMessage());
        }

        return newGame;
    }

    @PutMapping(path = "/game/join", consumes = "application/json", produces = "application/json")
    public Game joinGame(@RequestBody GameHandler gameHandler) throws Exception {
        Game newGame = null;
        try {
            newGame = gameBusinessService.joinGame(gameHandler.getProfileID(), gameHandler.getGameID());
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getConstraintViolations().iterator().next().getMessage());
        }

        return newGame;
    }

    @PutMapping(path = "/game/deregister", consumes = "application/json", produces = "application/json")
    public void deregisterFromGame(@RequestBody GameHandler gameHandler) {
        gameBusinessService.unregisterFromGame(gameHandler);
    }

    @PutMapping(path = "/game/state", consumes = "application/json", produces = "application/json")
    public Game updateGameState(@RequestBody GameHandler gameHandler) throws Exception {
        Game newGame = null;
        try {
            newGame = gameBusinessService.updateGameState(gameHandler);
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getConstraintViolations().iterator().next().getMessage());
        }

        return newGame;
    }

    @PutMapping(path = "/push", consumes = "application/json", produces = "application/json")
    public Player pushCardToPlayer(@RequestBody PushHandler pushHandler) throws Exception {
        Player player = null;
        try {
            player = gameBusinessService.pushCard(pushHandler);
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }

        return player;
    }

    @PutMapping(path = "/pass", consumes = "application/json", produces = "application/json")
    public void passTurn(@RequestBody GameHandler gameHandler) throws Exception{
        try {
            gameBusinessService.pass(gameHandler);
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getConstraintViolations().iterator().next().getMessage());
        }
    }

    @PutMapping(path = "/doMove", consumes = "application/json", produces = "application/json")
    public Game doMove(@RequestBody MoveHandler moveHandler) {
        Game game = null;
        try {
            game = gameBusinessService.determineCombination(moveHandler);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
        return game;
    }

}
