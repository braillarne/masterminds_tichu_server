package rocks.process.acrm.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rocks.process.acrm.data.domain.*;
import rocks.process.acrm.data.repository.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class GameBusinessService {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CombinationRepository combinationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DeckRepository deckRepository;

    public Deck createDeck() {
        Deck tempDeck = new Deck();

        //TODO Save method for Cards

        //Add Jade
        for (int i = Card.getMIN_RANK(); i < Card.getMAX_RANK(); i++) {
            Card tempcard = new Card(i, Suit.JADE);
            tempDeck.getCards().add(tempcard);
        }

        //Add Sword
        for (int i = Card.getMIN_RANK(); i < Card.getMAX_RANK(); i++) {
            Card tempcard = new Card(i, Suit.SWORD);
            tempDeck.getCards().add(tempcard);

        }

        //Add Pagoda
        for (int i = Card.getMIN_RANK(); i < Card.getMAX_RANK(); i++) {
            Card tempcard = new Card(i, Suit.PAGODA);
            tempDeck.getCards().add(tempcard);
        }

        //Add Star
        for (int i = Card.getMIN_RANK(); i < Card.getMAX_RANK(); i++) {
            Card tempcard = new Card(i, Suit.STAR);
            tempDeck.getCards().add(tempcard);
        }

        //Shuffle the deck
        Collections.shuffle(tempDeck.getCards());

        //TODO Save deck method

        return tempDeck;
    }


    @Autowired
    private GameRepository gameRepository;

    public Game createGame(Long profileID, String name) {
        Game tempGame = gameRepository.save(new Game());
        Deck tempDeck = deckRepository.save(createDeck());
        Player tempPlayer = createPlayer(profileID, name, tempGame);
        playerRepository.save(tempPlayer);
        List<Player> players = new ArrayList<>();
        players.add(tempPlayer);
        tempGame.setPlayers(players);
        tempGame.setDeck(tempDeck);
        return gameRepository.save(tempGame);
    }

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ResultRepository resultRepository;

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public void saveGame(Game game) throws Exception {
        try {
            gameRepository.save(game);
        } catch (Exception e) {
            throw new Exception("Invalid Game.");
        }
    }

    public void savePlayer(Player player) throws Exception {
        playerRepository.save(player);
    }

    public Player createPlayer(Long profileID, String name, Game game){
        Player tempPlayer = new Player();

        tempPlayer.setProfileID(profileID);
        tempPlayer.setName(name);
        tempPlayer.setHost(true);
        //TODO Set player to team. Not possible yet.
        //TODO Link player with profile
        tempPlayer.setGame(game);
        return tempPlayer;
    }

    public Player findOnePlayerByName(String name) {
        return playerRepository.findByName(name);
    }


    public boolean isPair(List<Card> transmittedcards) {
        if (transmittedcards.size() == 2 && transmittedcards.get(0).getRank() == transmittedcards.get(1).getRank()) {
            return true;

        } else return false;
    }

    public boolean isTriple(List<Card> transmittedcards) {
        if (transmittedcards.size() == 3
                && transmittedcards.get(0).getRank() == transmittedcards.get(1).getRank()
                && transmittedcards.get(0).getRank() == transmittedcards.get(2).getRank()) {
            return true;

        } else return false;
    }

    public boolean isRunningPair(List<Card> transmittedCards) {
        transmittedCards.sort(Comparator.comparing(Card::getRank));
        if (transmittedCards.size() < 4) return false;
        int counter = 0;
        for (int i = 1; i < transmittedCards.size() - 1; i = i + 2) {
            if (transmittedCards.get(i).getRank() + 1 == transmittedCards.get(i + 1).getRank()) {
                counter++;
            }
        }
        //n pairs take n-1 comparisons. We determine n pairs by dividing with two.
        if (counter == transmittedCards.size() / 2 - 1) return true;
        return false;
    }

    public boolean isFullHouse(List<Card> transmittedCards) {
        if (transmittedCards.size() != 5) return false;
        transmittedCards.sort(Comparator.comparing(Card::getRank));

        //Check xxxyy
        if (transmittedCards.get(0).getRank() == transmittedCards.get(1).getRank()
                && transmittedCards.get(1).getRank() == transmittedCards.get(2).getRank()
                && transmittedCards.get(3).getRank() == transmittedCards.get(4).getRank()) {

            return true;
        }


        //Check xxyyy
        if (transmittedCards.get(0).getRank() == transmittedCards.get(1).getRank()
                && transmittedCards.get(2).getRank() == transmittedCards.get(3).getRank()
                && transmittedCards.get(3).getRank() == transmittedCards.get(4).getRank()) {

            return true;
        }

        return false;

    }

    public boolean isRow(List<Card> transmittedCards) {
        int counter = 0;
        if (transmittedCards.size() < 5) return false;

        for (int i = 0; i < transmittedCards.size() - 1; i++) {
            if (transmittedCards.get(i).getRank() + 1 == transmittedCards.get(i + 1).getRank()) {
                counter++;

            }

        }
        //n sequence elements need n-1 comparisons
        if (counter + 1 == transmittedCards.size()) return true;

        return false;
    }

    public boolean isBomb(List<Card> transmittedCards) {
        int counter = 0;

        if (transmittedCards.size() == 4) {
            for (int i = 0; i < transmittedCards.size() - 1; i++) {
                if (transmittedCards.get(i).getRank() == transmittedCards.get(i + 1).getRank()) {
                    counter++;
                }
            }
            //n elements of a sequence need n-1 comparisons
            if (counter + 1 == transmittedCards.size()) return true;
        }

        if (transmittedCards.size() >= 5) {
            for (int i = 0; i < transmittedCards.size() - 1; i++) {
                if (transmittedCards.get(i).getRank() + 1 == transmittedCards.get(i + 1).getRank()
                        && transmittedCards.get(i).getSuit().equals(transmittedCards.get(i + 1).getSuit())) {
                    counter++;
                }
            }
            if (counter + 1 == transmittedCards.size()) return true;
        }

        return false;
    }


    public List<Combination> addPossiblePairsToPlayableCombinations(List<Card> hand) {
        List<Combination> combinationList = new ArrayList<>();

        for (int i = 0; i < hand.size(); i++) {
            for (int j = i + 1; j < hand.size(); j++) {
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
            for (int i = 0; i < pairs.size() - 1; i++) {
                List<Card> temp = new ArrayList<>();
                if (pairs.get(i).getMainRank() + 1 == pairs.get(i + 1).getMainRank()) {
                    if (!temp.contains(pairs.get(i).getCards())) {
                        for (Card c : pairs.get(i).getCards()
                        ) {
                            temp.add(c);
                        }
                    }
                    if (!temp.contains(pairs.get(i + 1).getCards())) {
                        for (Card c : pairs.get(i + 1).getCards()
                        ) {
                            temp.add(c);
                        }
                    }

                }
                int score = 0;
                for (Card c : temp
                ) {
                    score += c.getRank();
                }
                runningPairs.add(new Combination(temp, CombinationType.RUNPAIR, score));
            }
        }

        return runningPairs;
    }
}
