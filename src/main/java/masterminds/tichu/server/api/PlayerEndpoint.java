package masterminds.tichu.server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import masterminds.tichu.server.business.service.PlayerService;
import masterminds.tichu.server.data.domain.Player;

/**
 * Author(S): Nelson Braillard
 */
@RestController
@RequestMapping(path = "/api")
public class PlayerEndpoint {

    @Autowired
    private PlayerService playerService;

    @GetMapping(path = "/player/{playerName}", produces = "application/json")
    public Player getPlayer(@PathVariable(value = "playerName") String playerName) {
        Player player = null;
        try {
            player = playerService.findOnePlayerByName(playerName);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return player;
    }

    @GetMapping(path = "/player/id/{playerID}", produces = "application/json")
    public Player getPlayerByID(@PathVariable(value = "playerID") String playerID) {
        Player player = null;
        try {
            player = playerService.findOneByID(Long.parseLong(playerID));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return player;
    }
}
