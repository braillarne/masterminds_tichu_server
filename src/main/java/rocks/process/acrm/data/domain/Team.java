package rocks.process.acrm.data.domain;

import javax.persistence.*;

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
}
