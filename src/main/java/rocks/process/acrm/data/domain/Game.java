package rocks.process.acrm.data.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Game {
    @Id
    @GeneratedValue
    private Long gameId;
    @OneToMany(mappedBy = "game")
    private List<Player> players;
    private int currentRound;
    private int maxRound;
    private State state;
    public Game() {
        this.currentRound = 0;
        this.state = State.OPEN;
    }

    public void addPlayer(Player player){
        this.players.add(player);
    }

    public void removePlayer(Player player){
        this.players.remove(player);
    }

    public Long getId() { return gameId; }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }
}
