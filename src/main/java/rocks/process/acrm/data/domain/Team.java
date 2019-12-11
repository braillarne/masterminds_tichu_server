package rocks.process.acrm.data.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

/**
 * Author(S): Nelson Braillard
 */
@Entity
public class Team {
    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Id
    @GeneratedValue
    private Long teamId;
    private int score;
    @ManyToMany
    private List<Player> players;

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    @ManyToOne
    @JsonBackReference(value = "team-game")
    private Game game;
}
