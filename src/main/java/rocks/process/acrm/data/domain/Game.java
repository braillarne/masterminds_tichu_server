package rocks.process.acrm.data.domain;

import java.util.List;

public class Game {
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

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }
}
