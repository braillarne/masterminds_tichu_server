package rocks.process.acrm.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import rocks.process.acrm.data.domain.Player;
import rocks.process.acrm.data.repository.*;

public class GameBusinessService {

    @Autowired
    private PlayerRepository playerRepository;

    public void savePlayer(Player player) throws Exception {
        playerRepository.save(player);
    }

    public Player findOnePlayerByName(String name) {
        return playerRepository.findByName(name);
    }


    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CombinationRepository combinationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ResultRepository resultRepository;


}
