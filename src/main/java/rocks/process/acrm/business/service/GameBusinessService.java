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


        //Add Jade
        for (int i = Card.getMIN_RANK(); i < Card.getMAX_RANK(); i++) {
            Card tempcard = new Card(i, Suit.JADE);
            tempDeck.getCards().add(tempcard);
            cardRepository.save(tempcard);
        }

        //Add Sword
        for (int i = Card.getMIN_RANK(); i < Card.getMAX_RANK(); i++) {
            Card tempcard = new Card(i, Suit.SWORD);
            tempDeck.getCards().add(tempcard);
            cardRepository.save(tempcard);


        }

        //Add Pagoda
        for (int i = Card.getMIN_RANK(); i < Card.getMAX_RANK(); i++) {
            Card tempcard = new Card(i, Suit.PAGODA);
            tempDeck.getCards().add(tempcard);
            cardRepository.save(tempcard);

        }

        //Add Star
        for (int i = Card.getMIN_RANK(); i < Card.getMAX_RANK(); i++) {
            Card tempcard = new Card(i, Suit.STAR);
            tempDeck.getCards().add(tempcard);
            cardRepository.save(tempcard);

        }

        //Shuffle the deck
        Collections.shuffle(tempDeck.getCards());

        deckRepository.save(tempDeck);
        return tempDeck;
    }


    @Autowired
    private GameRepository gameRepository;

    public Game initalizeGame(Game game){
      // Game game = gameRepository.findByGameId(id);
        //Add teams to the players
        setTeamToPlayer(game);
        initalizeRound(game);
        return game;
    }

    public void initalizeRound(Game game){
        Deck tempDeck = deckRepository.save(createDeck());
        game.setDeck(tempDeck);
        //Distribute cards to different players
        int counter = 13;
        for(int i = 0; i<game.getPlayers().size();i++){
            for(int t = counter - 13; t<counter;t++){
                Card tempcard = game.getDeck().getCards().get(t);
                Player tempplayer = game.getPlayers().get(i);

                tempplayer.addOneCardToHand(tempcard);

            }
            counter = counter +14;
        }
    }

    public Game createGame(Long profileID, String name) {
        Profile p = profileRepository.findProfileById(profileID);
        Game tempGame = gameRepository.save(new Game());
        Player tempPlayer = createPlayer(profileID, p.getUsername(), tempGame);
        playerRepository.save(tempPlayer);
        List<Player> players = new ArrayList<>();
        players.add(tempPlayer);
        tempGame.setPlayers(players);
        tempGame.setName(name);
        tempGame.setState(State.OPEN);
        return gameRepository.save(tempGame);
    }

    @Autowired
    private TeamRepository teamRepository;



    public void setTeamToPlayer(Game game){

        Player tempplayer;

        Team team1 = new Team();
        Team team2 = new Team();

        team1.setScore(0);
        team2.setScore(0);


        //Add players to team1
        tempplayer = game.getPlayers().get(0);
        tempplayer.setTeam(team1);
        playerRepository.save(tempplayer);

        tempplayer = game.getPlayers().get(1);
        tempplayer.setTeam(team1);
        playerRepository.save(tempplayer);

        //Add players to team 2
        tempplayer = game.getPlayers().get(2);
        tempplayer.setTeam(team2);
        playerRepository.save(tempplayer);

        tempplayer = game.getPlayers().get(3);
        tempplayer.setTeam(team1);
        playerRepository.save(tempplayer);



        teamRepository.save(team1);
        teamRepository.save(team2);




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

    public boolean verificateStartofGame(Game game){

        if(game.getPlayers().size()==4) return true;

        return false;
    }




    public Game joinGame(Long profileID, Long gameID){
        Profile profile = profileRepository.findProfileById(profileID);
        Game game = gameRepository.findByGameId(gameID);
        Player tempPlayer = createPlayer(profileID, profile.getUsername(), game);

        List<Player> playerList = game.getPlayers();
        playerList.add(playerRepository.save(tempPlayer));
        game.setPlayers(playerList);
        return gameRepository.save(game);
    }

    public Game updateGameState(GameHandler gh) throws Exception{
        Game tempGame = gameRepository.findByGameId(gh.getGameID());
        if(gh.getGameState().equals(State.RUNNING)){
            try {
                Player player = playerRepository.findOnePlayerById(gh.getPlayerID());
                if(player.isHost()){
                    tempGame.setState(gh.getGameState());

                    tempGame = initalizeGame(tempGame);

                    return gameRepository.save(tempGame);
                }else {
                    throw new Exception("Need to be host of the game.");
                }
            }catch (Exception e){
                throw new Exception("Need to be host of the game.");
            }
        }else {
            tempGame.setState(gh.getGameState());
        }
        return gameRepository.save(tempGame);
    }

    public void savePlayer(Player player) throws Exception {
        playerRepository.save(player);
    }

    public Player createPlayer(Long profileID, String name, Game game){
        Player tempPlayer = new Player();

        tempPlayer.setProfileID(profileID);
        tempPlayer.setName(name);
        // Checks if there is already players involved. If not then the created player becomes the owner
        tempPlayer.setHost(game.getPlayers()==null);
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
