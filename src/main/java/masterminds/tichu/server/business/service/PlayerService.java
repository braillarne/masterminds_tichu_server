package masterminds.tichu.server.business.service;

import masterminds.tichu.server.data.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import masterminds.tichu.server.data.domain.Player;

/**
 * Author(S): Nelson Braillard
 */
@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public void savePlayer(Player player) throws Exception {
        playerRepository.save(player);
    }

    public Player findOnePlayerByName(String name){
        return playerRepository.findByName(name);
    }

    public Player findOneByID(Long id){
        return playerRepository.findOnePlayerById(id);
    }
}
