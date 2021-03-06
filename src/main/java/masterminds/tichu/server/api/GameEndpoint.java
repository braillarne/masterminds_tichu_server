package masterminds.tichu.server.api;

import masterminds.tichu.server.business.service.GameBusinessService;
import masterminds.tichu.server.data.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;

/**
 * Author(s): Nelson Braillard
 */
@RestController
@RequestMapping(path = "/api")
public class GameEndpoint {

    @Autowired
    private GameBusinessService gameBusinessService;

    @GetMapping(path = "/game", consumes = "application/json", produces = "application/json")
    public Game getGame(@RequestBody GameHandler gameHandler) {
        Game game = null;
        try {
            game = gameBusinessService.getGame(gameHandler.getGameID());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return game;
    }

    @GetMapping(path = "/game/{id}", consumes = "application/json", produces = "application/json")
    public Game getGameByID(@PathVariable(value = "id") String id) {
        Game game = null;
        try {
            game = gameBusinessService.getGame(Long.parseLong(id));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return game;
    }

    @PostMapping(path = "/game", consumes = "application/json", produces = "application/json")
    public Game postGame(@RequestBody Game game) throws Exception {
        try {
            gameBusinessService.saveGame(game);
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getConstraintViolations().iterator().next().getMessage());
        }

        return game;
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

    @PutMapping(path = "/game/push", consumes = "application/json", produces = "application/json")
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
            game = gameBusinessService.doMove(moveHandler);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
        }
        return game;
    }

    @DeleteMapping(path = "game/{id}", consumes = "application/json", produces = "application/json")
    public void deleteGame(@PathVariable(value = "id") String id) {
        Game game = null;
        try{
            game = gameBusinessService.getGame(Long.parseLong(id));

            gameBusinessService.deleteGame(game);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
