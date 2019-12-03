package rocks.process.acrm.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rocks.process.acrm.data.domain.*;
import rocks.process.acrm.data.repository.*;

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

    public Game getGame(Long id) {
        return gameRepository.findByGameId(id);
    }

    public void saveGame(Game game) {
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

    public Game initializeGame(Game game){

        return initializeRound(setTeamToPlayer(game));
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

        //Save everything what was modified
        //game.getPlayers().forEach(p -> playerRepository.save(p));

        return gameRepository.save(game);
    }

    public Player pushCard(PushHandler pushHandler) {
        Card pushedCard = cardRepository.getOne(pushHandler.getCardID());
        Player sender = playerRepository.getOne(pushHandler.getSenderID());
        Player receiver = playerRepository.getOne(pushHandler.getReceiverID());

        receiver.setReceivedCardCounter(receiver.getReceivedCardCounter()+1);
        receiver.getHand().add(pushedCard);

        sender.getHand().remove(pushedCard);

        playerRepository.save(receiver);

        return playerRepository.save(sender);
    }

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

    public void passToken(GameHandler gameHandler){
        Player currentPlayer = playerRepository.getOne(gameHandler.getPlayerID());
        Game currentGame = gameRepository.getOne(gameHandler.getGameID());

        int currentIndex = currentGame.getPlayers().indexOf(currentPlayer);

        for(int i = currentIndex; i<currentGame.getPlayers().size();i++) {
            if(currentGame.getPlayers().get(i+1).getHand()!=null && currentGame.getPlayers().get(i+1).getHand().size()>0){
                currentGame.getPlayers().get(i).setPlaying(false);
                currentGame.getPlayers().get(i+1).setPlaying(true);

                playerRepository.save(currentGame.getPlayers().get(i));
                playerRepository.save(currentGame.getPlayers().get(i+1));
                break;
            }
            if(i==currentGame.getPlayers().size()&& !currentGame.getPlayers().get(i).getId().equals(gameHandler.getPlayerID())){
                i=0;
            }
        }
    }

    public boolean isEndOfTrick(GameHandler gameHandler) {

        Game currentGame = gameRepository.getOne(gameHandler.getGameID());

        if (currentGame.getPassCounter() == 3) {


            endOfTrick(gameHandler);



            return true;
        }

        return false;

    }

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


        }


        return false;
    }

    public void endOfRound(GameHandler gameHandler, ArrayList<Player> loosers){

        Player currentPlayer = playerRepository.getOne(gameHandler.getPlayerID());
        Game currentGame = gameRepository.getOne(gameHandler.getGameID());
        Player looser = loosers.get(0);
        Player winner = playerRepository.findOnePlayerById(currentGame.getWinnerID());
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

        if(isEndOfGame(gameHandler)==false){

            initializeRound(currentGame);
        }








    }

    public int scoreCards(List<Card> cards){

        int score = 0;

        for(Card c:cards){

            if(c.getRank()==10||c.getRank()==13)score = score + 10;

            if(c.getRank()==5) score = score +5;



        }

        return score;


    }

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

        gameRepository.delete(game);

        return gameToBeReturned;
    }



    @Autowired
    private TeamRepository teamRepository;

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

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public boolean verificateStartofGame(Game game) {

        if(game.getPlayers().size()==4) return true;

        return false;
    }

    public void savePlayer(Player player) {
        playerRepository.save(player);
    }

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

    public Game determineCombination(MoveHandler moveHandler) {

        Player currentPlayer = playerRepository.findOnePlayerById(moveHandler.getPlayerID());
        Game currentGame = gameRepository.getOne(currentPlayer.getGame().getId());
        boolean isValid = false;

        if(currentPlayer.isPlaying()) {

            String currentCombination = null;
            if(moveHandler.getCombinationType()!=null){

                currentCombination = moveHandler.getCombinationType().toString();
            } else {

                currentCombination = currentGame.getCurrentCombination().getCombinationType().toString();
            }

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

        }

        if(isValid){
            return createCombinationFromCards(moveHandler);
        } else {
            return currentGame;
        }
    }

    public Game createCombinationFromCards(MoveHandler moveHandler) {

        Player currentPlayer = playerRepository.findOnePlayerById(moveHandler.getPlayerID());
        Game currentGame = gameRepository.getOne(currentPlayer.getGame().getId());

        int allranks = calculateCombinationScore(moveHandler.getCards());

        if (currentGame.getCurrentCombination()==null||allranks > currentGame.getCurrentCombination().getMainRank()) {
            Combination tempcomb = null;

            if(currentGame.getCurrentCombination()==null){
                tempcomb = createCombination(moveHandler.getCards(), moveHandler.getCombinationType(), allranks, currentPlayer);
            } else {
                Combination currentCombination = currentGame.getCurrentCombination();
                tempcomb = createCombination(moveHandler.getCards(), currentCombination.getCombinationType(), allranks, currentPlayer);
            }
            combinationRepository.save(tempcomb);

            currentGame.setCurrentCombination(tempcomb);
            currentGame.getPlayedCards().addAll(moveHandler.getCards());
            gameRepository.save(currentGame);

            //currentPlayer.getHand().removeAll(moveHandler.getCards());
            for (Card c:moveHandler.getCards()) {
                currentPlayer.getHand().remove(c);
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

    public void isWinner(Player potentialWinner, Game currentGame){

        if(potentialWinner.getHand().size()==0&&currentGame.getWinnerID()==null){

            currentGame.setWinnerID(potentialWinner.getId());
            gameRepository.save(currentGame);



        }



    }

    public Boolean isSingle(MoveHandler moveHandler) {
        Player currentPlayer = playerRepository.findOnePlayerById(moveHandler.getPlayerID());

        if (moveHandler.getCards().size() == 1) {
            return true;
        }

        return false;
    }

    public Boolean isPair(MoveHandler moveHandler) {

        if (moveHandler.getCards().size() == 2 && moveHandler.getCards().get(0).getRank() == moveHandler.getCards().get(1).getRank()) {
            return true;


        } else return false;
    }

    public boolean isTriple(MoveHandler moveHandler) {
        if (moveHandler.getCards().size() == 3
                && moveHandler.getCards().get(0).getRank() == moveHandler.getCards().get(1).getRank()
                && moveHandler.getCards().get(0).getRank() == moveHandler.getCards().get(2).getRank()) {

            return true;

        } else return false;
    }

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

    public int calculateCombinationScore(List<Card> cards) {

        //Add together main ranks of all the cards
        int allranks = 0;
        for (int i = 0; i < cards.size(); i++) {

            allranks = allranks + cards.get(i).getRank();
        }

        return allranks;
    }

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
