package masterminds.tichu.server.data.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Author(S): Nelson Braillard
 */
@Entity
public class Result {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean getIsWinner() {
        return isWinner;
    }

    public void setIsWinner(boolean isWinner) {
        this.isWinner = isWinner;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Id
    @GeneratedValue
    private Long id;
    private String date;
    private int score;
    private boolean isWinner;
    @ManyToOne
    @JsonBackReference
    private Profile profile;
}
