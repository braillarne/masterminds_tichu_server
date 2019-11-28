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
    @OneToOne
    @JsonBackReference(value = "combination-game")
    private Combination currentCombination;
    private int maxRound;
    private State state;
    private int passCounter;

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

    public int getMaxRound() {
        return maxRound;
    }

    public void setMaxRound(int maxRound) {
        this.maxRound = maxRound;
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
