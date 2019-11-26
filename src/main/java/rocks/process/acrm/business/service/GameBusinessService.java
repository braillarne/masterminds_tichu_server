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

    public Combination createCombination(List<Card> cards, CombinationType combinationType, int mainRank, Player player) {
        Combination tempComb = new Combination();
        tempComb.setCards(cards);
        tempComb.setCombinationType(combinationType);
        tempComb.setMainRank(mainRank);
        tempComb.setPlayer(player);
        combinationRepository.save(tempComb);

        return tempComb;

    }

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DeckRepository deckRepository;

    public List<Card> createDeck() {
        List<Card> deck = new ArrayList<>();

        //Add Jade
        for (int i = Card.getMIN_RANK(); i < Card.getMAX_RANK(); i++) {
            Card tempcard = new Card();
            tempcard.setRank(i);
            tempcard.setSuit(Suit.JADE);
            cardRepository.save(tempcard);
            deck.add(tempcard);
        }

        //Add Sword
        for (int i = Card.getMIN_RANK(); i < Card.getMAX_RANK(); i++) {
            Card tempcard = new Card();
            tempcard.setRank(i);
            tempcard.setSuit(Suit.SWORD);
            cardRepository.save(tempcard);
            deck.add(tempcard);
        }

        //Add Pagoda
        for (int i = Card.getMIN_RANK(); i < Card.getMAX_RANK(); i++) {
            Card tempcard = new Card();
            tempcard.setRank(i);
            tempcard.setSuit(Suit.PAGODA);
            cardRepository.save(tempcard);
            deck.add(tempcard);

        }

        //Add Star
        for (int i = Card.getMIN_RANK(); i < Card.getMAX_RANK(); i++) {
            Card tempcard = new Card();
            tempcard.setRank(i);
            tempcard.setSuit(Suit.STAR);
            cardRepository.save(tempcard);
            deck.add(tempcard);
        }

        //Shuffle the deck
        Collections.shuffle(deck);
        return deck;
    }


    @Autowired
    private GameRepository gameRepository;

    public Game initializeGame(Game game){
        // Game game = gameRepository.findByGameId(id);
        //Add teams to the players
        setTeamToPlayer(game);
        initializeRound(game);
        return game;
    }

    public void initializeRound(Game game){
        List<Card> deck = createDeck();
        //Distribute cards to different players
        for (int i = 0; ; i++) {
            if (i == game.getPlayers().size()) {

                i = 0;
            }

            game.getPlayers().get(i).addOneCardToHand(deck.get(0));

            deck.remove(0);


            if (deck.size() == 0) break;

        }

        //Save everything what was modified
        game.getPlayers().forEach(p -> playerRepository.save(p));

        gameRepository.save(game);


    }

    public Game createGame(Long profileID, String name) {
        Profile p = profileRepository.findProfileById(profileID);
        Game tempGame = gameRepository.save(new Game());
        Player tempPlayer = createPlayer(profileID, p.getAvatar(), p.getUsername(), tempGame);
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

        teamRepository.save(team1);
        teamRepository.save(team2);

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
        Player tempPlayer = createPlayer(profileID, profile.getAvatar(), profile.getUsername(), game);

        List<Player> playerList = game.getPlayers();
        playerList.add(playerRepository.save(tempPlayer));
        game.setPlayers(playerList);
        return gameRepository.save(game);
    }

    public Game updateGameState(GameHandler gh) throws Exception{
        Game tempGame = gameRepository.findByGameId(gh.getGameID());
        if(gh.getGameState().equals(State.RUNNING)||gh.getGameState().equals(State.CLOSED)){
            try {
                Player player = playerRepository.findOnePlayerById(gh.getPlayerID());
                if(player.isHost()){
                    tempGame.setState(gh.getGameState());

                    tempGame = initializeGame(tempGame);

                    return gameRepository.save(tempGame);
                }else {
                    throw new Exception("Need to be host of the game.");
                }
            }catch (Exception e){


                throw new Exception(e.getMessage());
            }
        }else {
            tempGame.setState(gh.getGameState());
        }
        return gameRepository.save(tempGame);
    }

    public void unregisterFromGame(GameHandler gameHandler) {
        Game game = gameRepository.findByGameId(gameHandler.getGameID());
        Player player = playerRepository.findOnePlayerById(gameHandler.getPlayerID());

        if(player.isHost()){
            if(game.getPlayers().size()>1){
                Player newHost = game.getPlayers().get(1);
                newHost.setHost(true);
                playerRepository.save(newHost);
                playerRepository.delete(player);
            }else {
                playerRepository.delete(player);
                game.setState(State.CLOSED);
                gameRepository.save(game);
            }
        } else {
            playerRepository.delete(player);
        }
    }

    public void savePlayer(Player player) throws Exception {
        playerRepository.save(player);
    }

    public Player createPlayer(Long profileID, int avatarID, String name, Game game){
        Player tempPlayer = new Player();

        tempPlayer.setProfileID(profileID);
        tempPlayer.setAvatarID(avatarID);
        tempPlayer.setName(name);
        // Checks if there is already players involved. If not then the created player becomes the owner
        tempPlayer.setHost(game.getPlayers()==null);
        tempPlayer.setGame(game);
        return tempPlayer;
    }

    public Player findOnePlayerByName(String name) {
        return playerRepository.findByName(name);
    }


    public Boolean isPair(List<Card> transmittedcards, long id) {
        Player player = playerRepository.findOnePlayerById(id);
        ArrayList<Card> tempPair = new ArrayList<>();
        if (transmittedcards.size() == 2 && transmittedcards.get(0).getRank() == transmittedcards.get(1).getRank()) {

            transmittedcards.forEach(card -> tempPair.add(card));



            //Add together main ranks of all the cards
            int allranks = calculateCombinationScore(tempPair);


            Combination tempcomb = createCombination(tempPair, CombinationType.PAIR, allranks, player);

            player.getGame().setCurrentCombination(tempcomb);

            return true;


        } else return false;
    }

    public boolean isTriple(List<Card> transmittedcards, long id) {
        Player player = playerRepository.findOnePlayerById(id);
        ArrayList<Card> tempTriple = new ArrayList<>();
        if (transmittedcards.size() == 3
                && transmittedcards.get(0).getRank() == transmittedcards.get(1).getRank()
                && transmittedcards.get(0).getRank() == transmittedcards.get(2).getRank()) {


            transmittedcards.forEach(card -> tempTriple.add(card));


//Add together main ranks of all the cards
            int allranks = calculateCombinationScore(tempTriple);


            Combination tempcomb = createCombination(tempTriple, CombinationType.TRIPLE, allranks, player);

            player.getGame().setCurrentCombination(tempcomb);

            return true;

        } else return false;
    }

    public boolean isRunningPair(List<Card> transmittedCards, long id) {
        Player player = playerRepository.findOnePlayerById(id);
        ArrayList<Card> tempRunning = new ArrayList<>();
        transmittedCards.sort(Comparator.comparing(Card::getRank));

        if (transmittedCards.size() < 4) return false;


        int counter = 0;
        for (int i = 1; i < transmittedCards.size() - 1; i = i + 2) {
            if (transmittedCards.get(i).getRank() + 1 == transmittedCards.get(i + 1).getRank()) {
                counter++;
            }
        }

        //n pairs take n-1 comparisons. We determine n pairs by dividing with two.
        if (counter == transmittedCards.size() / 2 - 1) {

            transmittedCards.forEach(card -> tempRunning.add(card));

            //Add together main ranks of all the cards
            int allranks = calculateCombinationScore(tempRunning);


            Combination tempcomb = createCombination(tempRunning, CombinationType.RUNPAIR, allranks, player);

            player.getGame().setCurrentCombination(tempcomb);





            return true;
        }
        return false;
    }

    public boolean isFullHouse(List<Card> transmittedCards, long id) {

        Player player = playerRepository.findOnePlayerById(id);
        ArrayList<Card> tempFull = new ArrayList<>();



        if (transmittedCards.size() != 5) return false;
        transmittedCards.sort(Comparator.comparing(Card::getRank));

        //Check xxxyy
        if (transmittedCards.get(0).getRank() == transmittedCards.get(1).getRank()
                && transmittedCards.get(1).getRank() == transmittedCards.get(2).getRank()
                && transmittedCards.get(3).getRank() == transmittedCards.get(4).getRank()) {


            transmittedCards.forEach(card -> tempFull.add(card));

            //Add together main ranks of all the cards
            int allranks = calculateCombinationScore(tempFull);


            Combination tempcomb = createCombination(tempFull, CombinationType.FULLHOUSE, allranks, player);

            player.getGame().setCurrentCombination(tempcomb);



            return true;
        }



        //Check xxyyy
        if (transmittedCards.get(0).getRank() == transmittedCards.get(1).getRank()
                && transmittedCards.get(2).getRank() == transmittedCards.get(3).getRank()
                && transmittedCards.get(3).getRank() == transmittedCards.get(4).getRank()) {


            transmittedCards.forEach(card -> tempFull.add(card));

            //Add together main ranks of all the cards
            int allranks = calculateCombinationScore(tempFull);


            Combination tempcomb = createCombination(tempFull, CombinationType.FULLHOUSE, allranks, player);

            player.getGame().setCurrentCombination(tempcomb);

            return true;
        }

        return false;

    }

    public boolean isRow(List<Card> transmittedCards, long id) {

        int counter = 0;
        Player player = playerRepository.findOnePlayerById(id);
        ArrayList<Card> tempRow = new ArrayList<>();




        if (transmittedCards.size() < 5) return false;

        for (int i = 0; i < transmittedCards.size() - 1; i++) {
            if (transmittedCards.get(i).getRank() + 1 == transmittedCards.get(i + 1).getRank()) {
                counter++;

            }

        }
        //n sequence elements need n-1 comparisons
        if (counter + 1 == transmittedCards.size()) {


            transmittedCards.forEach(card -> tempRow.add(card));

            //Add together main ranks of all the cards
            int allranks = calculateCombinationScore(tempRow);


            Combination tempcomb = createCombination(tempRow, CombinationType.ROW, allranks, player);

            player.getGame().setCurrentCombination(tempcomb);


            return true;
        }

        return false;
    }

    public int calculateCombinationScore(List<Card> cards) {

        //Add together main ranks of all the cards
        int allranks = 0;
        for (int i = 0; i < cards.size(); i++) {

            allranks = allranks + cards.get(i).getRank();
        }

        return allranks;
    }

    public boolean isBomb(List<Card> transmittedCards, long id) {


        int counter = 0;
        Player player = playerRepository.findOnePlayerById(id);
        ArrayList<Card> tempBomb = new ArrayList<>();





        if (transmittedCards.size() == 4) {
            for (int i = 0; i < transmittedCards.size() - 1; i++) {
                if (transmittedCards.get(i).getRank() == transmittedCards.get(i + 1).getRank()) {
                    counter++;
                }
            }
            //n elements of a sequence need n-1 comparisons
            if (counter + 1 == transmittedCards.size()) {


                transmittedCards.forEach(card -> tempBomb.add(card));

                //Add together main ranks of all the cards
                int allranks = calculateCombinationScore(tempBomb);


                Combination tempcomb = createCombination(tempBomb, CombinationType.BOMB, allranks, player);

                player.getGame().setCurrentCombination(tempcomb);


                return true;
            }
        }

        if (transmittedCards.size() >= 5) {
            for (int i = 0; i < transmittedCards.size() - 1; i++) {
                if (transmittedCards.get(i).getRank() + 1 == transmittedCards.get(i + 1).getRank()
                        && transmittedCards.get(i).getSuit().equals(transmittedCards.get(i + 1).getSuit())) {
                    counter++;
                }
            }
            if (counter + 1 == transmittedCards.size()) {

                transmittedCards.forEach(card -> tempBomb.add(card));

                //Add together main ranks of all the cards
                int allranks = calculateCombinationScore(tempBomb);


                Combination tempcomb = createCombination(tempBomb, CombinationType.BOMB, allranks, player);

                player.getGame().setCurrentCombination(tempcomb);


                return true;
            }
        }

        return false;
    }





}
