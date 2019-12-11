package rocks.process.acrm.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rocks.process.acrm.data.domain.*;
import rocks.process.acrm.data.repository.*;

import java.sql.Timestamp;
import java.util.*;

@Service
public class GameBusinessService {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private CardRepository cardRepository;

    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Autowired
    private CombinationRepository combinationRepository;

    /**
     * Author(s): Pascal Schaller
     *
     * @param cards
     * @param combinationType
     * @param mainRank
     * @param player
     * @return
     */
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
    private DeckRepository deckRepository;

    /**
     * Author(s): Nelson Braillard, Pascal Schaller
     *
     * @return
     */
    public List<Card> createDeck() {
        List<Card> deck = new ArrayList<>();

        //Add Jade
        for (int i = Card.getMIN_RANK(); i <= Card.getMAX_RANK(); i++) {
            Card tempcard = new Card();
            tempcard.setRank(i);
            tempcard.setSuit(Suit.JADE);
            cardRepository.save(tempcard);
            deck.add(tempcard);
        }

        //Add Sword
        for (int i = Card.getMIN_RANK(); i <= Card.getMAX_RANK(); i++) {
            Card tempcard = new Card();
            tempcard.setRank(i);
            tempcard.setSuit(Suit.SWORD);
            cardRepository.save(tempcard);
            deck.add(tempcard);
        }

        //Add Pagoda
        for (int i = Card.getMIN_RANK(); i <= Card.getMAX_RANK(); i++) {
            Card tempcard = new Card();
            tempcard.setRank(i);
            tempcard.setSuit(Suit.PAGODA);
            cardRepository.save(tempcard);
            deck.add(tempcard);

        }

        //Add Star
        for (int i = Card.getMIN_RANK(); i <= Card.getMAX_RANK(); i++) {
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

    /**
     * Author(s): Nelson Braillard
     *
     * @param id
     * @return
     */
    public Game getGame(Long id) {
        return gameRepository.findByGameId(id);
    }

    /**
     * Author(s): Nelson Braillard
     *
     * @param game
     */
    public void saveGame(Game game) {
        gameRepository.save(game);
    }

    /**
     * Author(s): Nelson Braillard
     *
     * @param profileID
     * @param name
     * @return
     */
    public Game createGame(Long profileID, String name) throws Exception{
        if(gameRepository.findAllByName(name).size()!=0){
            throw new Exception("Name already used");
        } else {

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
    }

    /**
     * Author(s): Nelson Braillard
     *
     * @param game
     * @return
     */
    public Game initializeGame(Game game){

        return initializeRound(setTeamToPlayer(game));
    }

    /**
     * Author(s): Nelson Braillard
     *
     * @param profileID
     * @param gameID
     * @return
     */
    public Game joinGame(Long profileID, Long gameID){
        Profile profile = profileRepository.findProfileById(profileID);
        Game game = gameRepository.findByGameId(gameID);
        Player tempPlayer = createPlayer(profileID, profile.getAvatar(), profile.getUsername(), game);

        List<Player> playerList = game.getPlayers();
        playerList.add(playerRepository.save(tempPlayer));
        game.setPlayers(playerList);
        return gameRepository.save(game);
    }

    /**
     * Author(s): Nelson Braillard
     *
     * @param gh
     * @return
     * @throws Exception
     */
    public Game updateGameState(GameHandler gh) throws Exception{
        Game tempGame = gameRepository.findByGameId(gh.getGameID());
        if(gh.getGameState().equals(State.RUNNING)||gh.getGameState().equals(State.CLOSED)){
            try {
                Player player = playerRepository.findOnePlayerById(gh.getPlayerID());
                if(player.isHost()){
                    tempGame.setState(gh.getGameState());

                    if(gh.getGameState().equals(State.RUNNING)) {
                        tempGame = initializeGame(tempGame);
                    }

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

    /**
     * Author(s): Nelson Braillard
     *
     * @param gameHandler
     */
    public void unregisterFromGame(GameHandler gameHandler) {
        Game game = gameRepository.findByGameId(gameHandler.getGameID());
        Player player = playerRepository.findOnePlayerById(gameHandler.getPlayerID());

        if(player.isHost()){
            if(game.getPlayers().size()>1){
                Player newHost = game.getPlayers().get(1);
                newHost.setHost(true);
                newHost.setPlaying(true);
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

    /**
     * Author(s): Nelson Braillard, Pascal Schaller
     *
     * @param game
     * @return
     */
    public Game initializeRound(Game game){
        List<Card> deck = createDeck();

        //Clear everything from previous game
        game.setCurrentCombination(null);
        game.setWinnerID(null);
        game.getPlayedCards().clear();
        game.setPassCounter(0);

        for(Player p: game.getPlayers()){

            p.getWonCards().clear();

        }

        //Distribute cards to different players
        for (int i = 0; ; i++) {
            if (i == game.getPlayers().size()) {

                i = 0;
            }

            game.getPlayers().get(i).addOneCardToHand(deck.get(0));
            deck.get(0).setPlayerAssociatedToHand(game.getPlayers().get(i));
            cardRepository.save(deck.get(0));
            playerRepository.save(game.getPlayers().get(i));
            gameRepository.save(game);

            deck.remove(0);

            if (deck.size() == 0) break;

        }

        return gameRepository.save(game);
    }

    /**
     * Author(s): Nelson Braillard
     *
     * @param pushHandler
     * @return
     */
    public Player pushCard(PushHandler pushHandler) {
        Card pushedCard = cardRepository.getOne(pushHandler.getCardID());
        Player sender = playerRepository.getOne(pushHandler.getSenderID());
        Player receiver = playerRepository.getOne(pushHandler.getReceiverID());

        receiver.setReceivedCardCounter(receiver.getReceivedCardCounter()+1);

        receiver.addOneCardToHand(pushedCard);
        sender.removeOneCardFromHand(pushedCard);

        pushedCard.setPlayerAssociatedToHand(receiver);

        cardRepository.save(pushedCard);
        playerRepository.save(receiver);

        return playerRepository.save(sender);
    }

    /**
     * Author(s): Nelson Braillard
     *
     * @param gameHandler
     */
    public void pass(GameHandler gameHandler) {
        Player currentPlayer = playerRepository.getOne(gameHandler.getPlayerID());

        if(currentPlayer.isPlaying()) {
            currentPlayer.getGame().setPassCounter(currentPlayer.getGame().getPassCounter() + 1);

            gameHandler.setGameID(currentPlayer.getGame().getId());
            if(!isEndOfTrick(gameHandler)){

                passToken(gameHandler);
            } else {
                endOfTrick(gameHandler);
            }
        }
    }

    /**
     * Author(s): Nelson Braillard
     *
     * @param gameHandler
     */
    public void passToken(GameHandler gameHandler){
        Player currentPlayer = playerRepository.getOne(gameHandler.getPlayerID());
        Game currentGame = gameRepository.getOne(gameHandler.getGameID());

        int currentIndex = currentGame.getPlayers().indexOf(currentPlayer);

        for(int i = currentIndex; i<currentGame.getPlayers().size();i++) {

            if(i==3){
                if(currentGame.getPlayers().get(0).getHand()!=null && currentGame.getPlayers().get(i+0).getHand().size()>0){
                    currentGame.getPlayers().get(i).setPlaying(false);
                    currentGame.getPlayers().get(i+1).setPlaying(true);

                    playerRepository.save(currentGame.getPlayers().get(i));
                    playerRepository.save(currentGame.getPlayers().get(i+1));
                    break;
                }
            }
             else if(currentGame.getPlayers().get(i+1).getHand()!=null && currentGame.getPlayers().get(i+1).getHand().size()>0){
                currentGame.getPlayers().get(i).setPlaying(false);
                currentGame.getPlayers().get(i+1).setPlaying(true);

                playerRepository.save(currentGame.getPlayers().get(i));
                playerRepository.save(currentGame.getPlayers().get(i+1));
                break;
            }
            if(i==currentGame.getPlayers().size() || !currentGame.getPlayers().get(i).getId().equals(gameHandler.getPlayerID())){
                i=0;
            }
        }
    }

    /**
     * Author(s): Pascal Schaller
     *
     * @param gameHandler
     * @return
     */
    public boolean isEndOfTrick(GameHandler gameHandler) {

        Game currentGame = gameRepository.getOne(gameHandler.getGameID());

        if (currentGame.getPassCounter() == 3) {


            endOfTrick(gameHandler);



            return true;
        }

        return false;

    }

    /**
     * Author(s): Pascal Schaller
     *
     * @param gameHandler
     */
    public void endOfTrick(GameHandler gameHandler){

        Game currentGame = gameRepository.getOne(gameHandler.getGameID());
        Player currentLeader = currentGame.getCurrentCombination().getPlayer();
        Player currentPlayer = playerRepository.findOnePlayerById(gameHandler.getPlayerID());

        currentLeader.setPlaying(true);
        currentPlayer.setPlaying(false);

        for (Card c: currentGame.getPlayedCards()) {
            currentLeader.getWonCards().add(c);
            c.setPlayerAssociatedToWon(currentLeader);
            cardRepository.save(c);
            playerRepository.save(currentLeader);
        }

        playerRepository.save(currentPlayer);

        currentGame.getPlayedCards().clear();
        currentGame.setCurrentCombination(null);
        gameRepository.save(currentGame);

    }

    /**
     * Author(s): Pascal Schaller
     *
     * @param gameHandler
     * @return
     */
    public boolean isEndOfRound(GameHandler gameHandler){

        Player currentPlayer = playerRepository.getOne(gameHandler.getPlayerID());
        Game currentGame = gameRepository.getOne(gameHandler.getGameID());
        ArrayList<Player> loosers = new ArrayList<>();



        for(int i = 0;i<currentGame.getPlayers().size();i++){

            if(currentGame.getPlayers().get(i).getHand().size()>0){

                loosers.add(currentGame.getPlayers().get(i));

            }



        }

        if (loosers.size()==1){

            endOfRound(gameHandler,loosers);

        } else if (loosers.size()==2&&loosers.get(0).getTeam()==loosers.get(1).getTeam()){
            loosers.get(0).getTeam().setScore(loosers.get(0).getTeam().getScore()+200);
            currentPlayer.setPlaying(false);
            currentGame.getCurrentCombination().getPlayer().setPlaying(true);

            playerRepository.save(currentPlayer);
            playerRepository.save(currentGame.getCurrentCombination().getPlayer());

            initializeRound(currentGame);
        }


        return false;
    }

    /**
     * Author(s): Pascal Schaller
     *
     * @param gameHandler
     * @param loosers
     */
    public void endOfRound(GameHandler gameHandler, ArrayList<Player> loosers){

        Player currentPlayer = playerRepository.getOne(gameHandler.getPlayerID());
        currentPlayer.setPlaying(false);
        playerRepository.save(currentPlayer);
        Game currentGame = gameRepository.getOne(gameHandler.getGameID());
        Player looser = loosers.get(0);
        Player winner = playerRepository.findOnePlayerById(currentGame.getWinnerID());
        winner.setPlaying(true);
        Team looserteam = looser.getTeam();

        //Give won tricks to winner

        for(Card c:looser.getHand()){

            winner.getWonCards().add(c);
            playerRepository.save(winner);
        }


        //Give won score from hand to opponent team
        int handscorelooser = scoreCards(looser.getHand());



        for(Team t:currentGame.getTeams()){

            if (t!=looser.getTeam()){
                t.setScore(handscorelooser);
                teamRepository.save(t);
            }
        }


        //Score the round
        for(Player p:currentGame.getPlayers()){

            int wonScoreFromTrick = scoreCards(p.getWonCards());
            p.getTeam().setScore(wonScoreFromTrick);
            teamRepository.save(p.getTeam());

        }

        if(!isEndOfGame(gameHandler)){
            initializeRound(currentGame);
        } else {
            endOfGame(currentGame);
        }

    }

    /**
     * Author(s): Pascal Schaller
     *
     * @param cards
     * @return
     */
    public int scoreCards(List<Card> cards){

        int score = 0;

        for(Card c:cards){

            if(c.getRank()==10||c.getRank()==13)score = score + 10;

            if(c.getRank()==5) score = score +5;



        }

        return score;


    }

    /**
     * Author(s): Nelson Braillard
     *
     * @param gameHandler
     * @return
     */
    public boolean isEndOfGame(GameHandler gameHandler){
        boolean isTheEnd = true;

        Game currentGame = gameRepository.getOne(gameHandler.getGameID());
        List<Team> teams = currentGame.getTeams();
        List<Team> teamsWithMoreThan1000pts = new ArrayList<>();

        for (Team t:teams ) {
            if(t.getScore()>=1000){
                teamsWithMoreThan1000pts.add(t);
            }
        }

        for(int i=0; i<teamsWithMoreThan1000pts.size()-1;i++){
            for(int j=i+1; j<teamsWithMoreThan1000pts.size();j++){
                if(teamsWithMoreThan1000pts.get(i).getScore()==teamsWithMoreThan1000pts.get(j).getScore()){
                    isTheEnd=false;
                    break;
                }
            }
        }

        if(teamsWithMoreThan1000pts.size()<1){
            isTheEnd = false;
        }

        return isTheEnd;
    }

    /**
     * Author(s): Nelson Braillard
     *
     * @param game
     * @return
     */
    private Game endOfGame(Game game) {
        Game gameToBeReturned = null;

        Team winners = null;
        int winningScore = 0;

        for (Team t:game.getTeams()) {
            if(t.getScore()>winningScore){
                winningScore = t.getScore();
                winners = t;
            }
        }

        game.setState(State.CLOSED);
        game.setEndOfTheGameMessage("Team " + winners.getTeamId() + " wons with " + winners.getScore() + "pts.");
        gameToBeReturned = gameRepository.save(game);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        for (Player p:game.getPlayers()) {
            Result result = new Result();
            Profile profile = profileRepository.findProfileById(p.getProfileID());

            result.setIsWinner(p.getTeam()==winners);
            result.setProfile(profile);
            result.setDate(timestamp.toString());

            profile.getResults().add(result);

            resultRepository.save(result);
            profileRepository.save(profile);
        }

        gameRepository.delete(game);

        return gameToBeReturned;
    }

    @Autowired
    private TeamRepository teamRepository;

    /**
     * Author(s): Nelson Braillard, Pascal Schaller
     *
     * @param game
     * @return
     */
    public Game setTeamToPlayer(Game game) {

        Team team1 = new Team();
        Team team2 = new Team();

        team1.setScore(0);
        team2.setScore(0);

        teamRepository.save(team1);
        teamRepository.save(team2);

        game.getPlayers().get(0).setTeam(team2);
        game.getPlayers().get(1).setTeam(team1);
        game.getPlayers().get(2).setTeam(team2);
        game.getPlayers().get(3).setTeam(team1);

        for (Player p : game.getPlayers()) { playerRepository.save(p); }
        gameRepository.save(game);

        List<Player> playersToBeAdded = new ArrayList<>();
        playersToBeAdded.add(game.getPlayers().get(0));
        playersToBeAdded.add(game.getPlayers().get(2));
        team2.setPlayers(playersToBeAdded);

        playersToBeAdded.clear();
        playersToBeAdded.add(game.getPlayers().get(1));
        playersToBeAdded.add(game.getPlayers().get(3));
        team1.setPlayers(playersToBeAdded);

        game.getTeams().add(team1);
        game.getTeams().add(team2);

        teamRepository.save(team1);
        teamRepository.save(team2);
        return  gameRepository.save(game);
    }


    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ResultRepository resultRepository;

    /**
     * Author(s): Nelson Braillard
     *
     * @return
     */
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    // TODO Delete
    public boolean verificateStartofGame(Game game) {

        if(game.getPlayers().size()==4) return true;

        return false;
    }

    /**
     * Author(s): Nelson Braillard
     *
     * @param player
     */
    public void savePlayer(Player player) {
        playerRepository.save(player);
    }

    /**
     * Author(s): Nelson Braillard
     *
     * @param profileID
     * @param avatarID
     * @param name
     * @param game
     * @return
     */
    public Player createPlayer(Long profileID, int avatarID, String name, Game game) {
        Player tempPlayer = new Player();

        tempPlayer.setProfileID(profileID);
        tempPlayer.setAvatarID(avatarID);
        tempPlayer.setName(name);
        // Checks if there is already players involved. If not then the created player becomes the owner
        tempPlayer.setHost(game.getPlayers() == null);
        tempPlayer.setPlaying(game.getPlayers() == null);
        tempPlayer.setGame(game);
        return tempPlayer;
    }

    // TODO: delete method if never used
    public Player findOnePlayerByName(String name) {
        return playerRepository.findByName(name);
    }

    /**
     * Author(s): Nelson Braillard, Pascal Schaller
     *
     * @param moveHandler
     * @return
     */
    public Game doMove(MoveHandler moveHandler) {

        Player currentPlayer = playerRepository.findOnePlayerById(moveHandler.getPlayerID());
        Game currentGame = gameRepository.getOne(currentPlayer.getGame().getId());


        boolean isValid = false;

        String currentCombination = null;
        if(currentPlayer.isPlaying()) {

            if (currentGame.getCurrentCombination() == null) {
                moveHandler = determineCombinationType(moveHandler);
                currentCombination = moveHandler.getCombinationType().toString();

            } else {
                currentCombination = currentGame.getCurrentCombination().getCombinationType().toString();

            }
        }

        isValid = validateCombination(currentCombination, moveHandler);

        if(isValid){
            return createCombinationFromCards(moveHandler);
        } else {
            return currentGame;
        }
    }

    /**
     * Author(s): Nelson Braillard
     *
     * @param moveHandler
     * @return
     */
    private MoveHandler determineCombinationType(MoveHandler moveHandler) {
        if(isSingle(moveHandler)){
            moveHandler.setCombinationType(CombinationType.SINGLE);
        }

        if (isPair(moveHandler)){
            moveHandler.setCombinationType(CombinationType.PAIR);
        }

        if (isRunningPair(moveHandler)) {
            moveHandler.setCombinationType(CombinationType.RUNPAIR);
        }

        if (isTriple(moveHandler)) {
            moveHandler.setCombinationType(CombinationType.TRIPLE);
        }

        if (isFullHouse(moveHandler)) {
            moveHandler.setCombinationType(CombinationType.FULLHOUSE);
        }

        if (isRow(moveHandler)) {
            moveHandler.setCombinationType(CombinationType.ROW);
        }

        if (isBomb(moveHandler)) {
            moveHandler.setCombinationType(CombinationType.BOMB);
        }

        return moveHandler;
    }

    /**
     * Author(s): Pascal Schaller
     *
     * @param currentCombination
     * @param moveHandler
     * @return
     */
    private Boolean validateCombination(String currentCombination, MoveHandler moveHandler) {
        boolean isValid = false;
        switch (currentCombination) {

            case "SINGLE":
                if (isSingle(moveHandler)) {
                    isValid = true;
                }
                break;

            case "PAIR":
                if (isPair(moveHandler)) {
                    isValid = true;
                }
                break;

            case "RUNPAIR":
                if (isRunningPair(moveHandler)) {
                    isValid = true;
                }
                break;

            case "TRIPLE":
                if (isTriple(moveHandler)) {
                    isValid = true;
                }
                break;

            case "FULLHOUSE":
                if (isFullHouse(moveHandler)) {
                    isValid = true;
                }

            case "ROW":
                if (isRow(moveHandler)) {
                    isValid = true;
                }
                break;

            case "BOMB":
                if (isBomb(moveHandler)) {
                    isValid = true;
                }
                break;
        }

        return isValid;
    }

    /**
     * Author(s): Pascal Schaller
     *
     * @param moveHandler
     * @return
     */
    public Game createCombinationFromCards(MoveHandler moveHandler) {

        Player currentPlayer = playerRepository.findOnePlayerById(moveHandler.getPlayerID());
        Game currentGame = gameRepository.getOne(currentPlayer.getGame().getId());

        int allranks = calculateCombinationScore(moveHandler.getCards());

        if (currentGame.getCurrentCombination()==null||allranks > currentGame.getCurrentCombination().getMainRank()) {
            Combination tempcomb = null;

            if(currentGame.getCurrentCombination()==null){
                tempcomb = createCombination(moveHandler.getCards(), moveHandler.getCombinationType(), allranks, currentPlayer);
                for (Card c:moveHandler.getCards()) {
                    c.setCombination(tempcomb);
                    cardRepository.save(c);
                }
            } else {
                Combination currentCombination = currentGame.getCurrentCombination();
                tempcomb = createCombination(moveHandler.getCards(), currentCombination.getCombinationType(), allranks, currentPlayer);
                for (Card c:moveHandler.getCards()) {
                    c.setCombination(tempcomb);
                    cardRepository.save(c);
                }
            }
            combinationRepository.save(tempcomb);

            currentGame.setCurrentCombination(tempcomb);
            currentGame.getPlayedCards().addAll(moveHandler.getCards());
            gameRepository.save(currentGame);

            //currentPlayer.getHand().removeAll(moveHandler.getCards());
            for (Card c:moveHandler.getCards()) {
                currentPlayer.getHand().remove(c);
                cardRepository.save(c);
            }

            currentPlayer = playerRepository.save(currentPlayer);

            //Check if player of playedCombination is the winner
            isWinner(currentPlayer,currentGame);


            GameHandler gameHandler = new GameHandler();
            gameHandler.setPlayerID(currentPlayer.getId());
            gameHandler.setGameID(currentGame.getGameId());
            passToken(gameHandler);
            currentGame.setPassCounter(0);
            gameRepository.save(currentGame);

            isEndOfTrick(gameHandler);
            isEndOfRound(gameHandler);
        }

        return currentGame;
    }

    /**
     * Author(s): Pascal Schaller
     *
     * @param potentialWinner
     * @param currentGame
     */
    public void isWinner(Player potentialWinner, Game currentGame){

        if(potentialWinner.getHand().size()==0&&currentGame.getWinnerID()==null){

            currentGame.setWinnerID(potentialWinner.getId());
            gameRepository.save(currentGame);

        }

    }

    /**
     * Author(s): Pascal Schaller
     *
     * @param moveHandler
     * @return
     */
    public Boolean isSingle(MoveHandler moveHandler) {
        Player currentPlayer = playerRepository.findOnePlayerById(moveHandler.getPlayerID());

        if (moveHandler.getCards().size() == 1) {
            return true;
        }

        return false;
    }

    /**
     * Author(s): Pascal Schaller
     *
     * @param moveHandler
     * @return
     */
    public Boolean isPair(MoveHandler moveHandler) {

        if (moveHandler.getCards().size() == 2 && moveHandler.getCards().get(0).getRank() == moveHandler.getCards().get(1).getRank()) {
            return true;


        } else return false;
    }

    /**
     * Author(s): Pascal Schaller
     *
     * @param moveHandler
     * @return
     */
    public boolean isTriple(MoveHandler moveHandler) {
        if (moveHandler.getCards().size() == 3
                && moveHandler.getCards().get(0).getRank() == moveHandler.getCards().get(1).getRank()
                && moveHandler.getCards().get(0).getRank() == moveHandler.getCards().get(2).getRank()) {

            return true;

        } else return false;
    }

    /**
     * Author(s): Pascal Schaller
     *
     * @param moveHandler
     * @return
     */
    public boolean isRunningPair(MoveHandler moveHandler) {


        moveHandler.getCards().sort(Comparator.comparing(Card::getRank));

        if (moveHandler.getCards().size() < 4) return false;


        int counter = 0;
        for (int i = 1; i < moveHandler.getCards().size() - 1; i = i + 2) {
            if (moveHandler.getCards().get(i).getRank() + 1 == moveHandler.getCards().get(i + 1).getRank()) {
                counter++;
            }
        }

        //n pairs take n-1 comparisons. We determine n pairs by dividing with two.
        if (counter == moveHandler.getCards().size() / 2 - 1) {

            return true;
        }
        return false;
    }

    /**
     * Author(s): Pascal Schaller
     *
     * @param moveHandler
     * @return
     */
    public boolean isFullHouse(MoveHandler moveHandler) {


        if (moveHandler.getCards().size() != 5) return false;
        moveHandler.getCards().sort(Comparator.comparing(Card::getRank));

        //Check xxxyy
        if (moveHandler.getCards().get(0).getRank() == moveHandler.getCards().get(1).getRank()
                && moveHandler.getCards().get(1).getRank() == moveHandler.getCards().get(2).getRank()
                && moveHandler.getCards().get(3).getRank() == moveHandler.getCards().get(4).getRank()) {

            return true;
        }


        //Check xxyyy
        if (moveHandler.getCards().get(0).getRank() == moveHandler.getCards().get(1).getRank()
                && moveHandler.getCards().get(2).getRank() == moveHandler.getCards().get(3).getRank()
                && moveHandler.getCards().get(3).getRank() == moveHandler.getCards().get(4).getRank()) {

            return true;
        }

        return false;

    }

    /**
     * Author(s): Pascal Schaller
     *
     * @param moveHandler
     * @return
     */
    public boolean isRow(MoveHandler moveHandler) {

        int counter = 0;


        if (moveHandler.getCards().size() < 5) return false;

        for (int i = 0; i < moveHandler.getCards().size() - 1; i++) {
            if (moveHandler.getCards().get(i).getRank() + 1 == moveHandler.getCards().get(i + 1).getRank()) {
                counter++;

            }

        }
        //n sequence elements need n-1 comparisons
        if (counter + 1 == moveHandler.getCards().size()) {

            return true;
        }

        return false;
    }

    /**
     * Author(s): Pascal Schaller
     *
     * @param cards
     * @return
     */
    public int calculateCombinationScore(List<Card> cards) {

        //Add together main ranks of all the cards
        int allranks = 0;
        for (int i = 0; i < cards.size(); i++) {

            allranks = allranks + cards.get(i).getRank();
        }

        return allranks;
    }

    /**
     * Author(s): Pascal Schaller
     *
     * @param moveHandler
     * @return
     */
    public boolean isBomb(MoveHandler moveHandler) {


        int counter = 0;


        if (moveHandler.getCards().size() == 4) {
            for (int i = 0; i < moveHandler.getCards().size() - 1; i++) {
                if (moveHandler.getCards().get(i).getRank() == moveHandler.getCards().get(i + 1).getRank()) {
                    counter++;
                }
            }
            //n elements of a sequence need n-1 comparisons
            if (counter + 1 == moveHandler.getCards().size()) {

                return true;
            }
        }

        if (moveHandler.getCards().size() >= 5) {
            for (int i = 0; i < moveHandler.getCards().size() - 1; i++) {
                if (moveHandler.getCards().get(i).getRank() + 1 == moveHandler.getCards().get(i + 1).getRank()
                        && moveHandler.getCards().get(i).getSuit().equals(moveHandler.getCards().get(i + 1).getSuit())) {
                    counter++;
                }
            }
            if (counter + 1 == moveHandler.getCards().size()) {

                return true;
            }
        }

        return false;
    }
}
