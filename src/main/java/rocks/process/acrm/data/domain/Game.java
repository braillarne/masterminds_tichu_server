package rocks.process.acrm.data.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

@Entity
public class Game {
    @Id
    @GeneratedValue
    private Long gameId;
    private String name;
    @OneToMany(mappedBy = "game")
    private List<Player> players;
    @ManyToMany
    private List<Team> teams;
    private int currentScore;
    @ManyToOne
    private Combination currentCombination;
    @ManyToMany
    private List<Card> playedCards;
    private Long winnerID;
    private State state;
    private int passCounter;
    private String endOfTheGameMessage;

    public String getEndOfTheGameMessage() {
        return endOfTheGameMessage;
    }

    public List<Card> getPlayedCards() {
        return playedCards;
    }

    public void setPlayedCards(List<Card> playedCards) {
        this.playedCards = playedCards;
    }

    public void setEndOfTheGameMessage(String endOfTheGameMessage) {
        this.endOfTheGameMessage = endOfTheGameMessage;
    }

    public int getPassCounter() {
        return passCounter;
    }

    public void setPassCounter(int passCounter) {
        this.passCounter = passCounter;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Long getWinnerID() {
        return winnerID;
    }

    public void setWinnerID(Long winnerID) {
        this.winnerID = winnerID;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void removePlayer(Player player) {
        this.players.remove(player);
    }

    public Long getId() {
        return gameId;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public Combination getCurrentCombination() {
        return currentCombination;
    }

    public void setCurrentCombination(Combination currentCombination) {
        this.currentCombination = currentCombination;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}
