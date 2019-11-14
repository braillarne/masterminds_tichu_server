package rocks.process.acrm.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rocks.process.acrm.data.domain.*;
import rocks.process.acrm.data.repository.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameBusinessService {

    @Autowired
    private PlayerRepository playerRepository;

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public void savePlayer(Player player) throws Exception {
        playerRepository.save(player);
    }

    public Player findOnePlayerByName(String name) {
        return playerRepository.findByName(name);
    }

    public void evaluatePlayableCombinationsFromPlayerHand(List<Card> hand) {
    }

    public List<Combination> addPossiblePairsToPlayableCombinations(List<Card> hand) {
        List<Combination> combinationList = new ArrayList<>();

        for (int i = 0; i < hand.size(); i++) {
            for (int j = i+1; j < hand.size(); j++) {
                if (hand.get(i).getRank() == hand.get(j).getRank()) {
                    List<Card> temp = new ArrayList<>();
                    temp.add(hand.get(i));
                    temp.add(hand.get(j));
                    Combination newPair = new Combination(temp, CombinationType.PAIR, hand.get(i).getRank());
                    combinationList.add(newPair);
                }
            }
        }
        return combinationList;
    }

    public List<Combination> addPossibleRunningPairs(List<Combination> pairs) {
        List<Combination> runningPairs = new ArrayList<>();

        if (pairs.size() < 2) {
            return runningPairs;
        } else {
            for (int i = 0; i < pairs.size()-1; i++) {
                List<Card> temp = new ArrayList<>();
                if (pairs.get(i).getMainRank() + 1 == pairs.get(i + 1).getMainRank()) {
                    if (!temp.contains(pairs.get(i).getCards())) {
                        for (Card c:pairs.get(i).getCards()
                             ) {
                            temp.add(c);
                        }
                    }
                    if (!temp.contains(pairs.get(i + 1).getCards())) {
                        for (Card c:pairs.get(i+1).getCards()
                        ) {
                            temp.add(c);
                        }
                    }

                }
                int score = 0;
                for (Card c:temp
                     ) {
                    score += c.getRank();
                }
                runningPairs.add(new Combination(temp, CombinationType.RUNPAIR, score));
            }
        }

        return runningPairs;
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
