package rocks.process.acrm.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rocks.process.acrm.data.domain.Player;
import rocks.process.acrm.data.repository.PlayerRepository;

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
