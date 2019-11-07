package rocks.process.acrm.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import rocks.process.acrm.business.service.PlayerService;
import rocks.process.acrm.data.domain.Player;

@RestController
@RequestMapping(path = "/api")
public class PlayerEndpoint {

    @Autowired
    private PlayerService playerService;

    @GetMapping(path = "/player/{playerName}", produces = "application/json")
    public ResponseEntity<Player> getPlayer(@PathVariable(value = "playerName") String playerName) {
        Player player = null;
        try {
            player = playerService.findOnePlayerByName(playerName);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.ok(player);
    }
}
