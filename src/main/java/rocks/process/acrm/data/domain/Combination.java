package rocks.process.acrm.data.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

@Entity
public class Combination {
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setCombinationType(CombinationType combinationType) {
        this.combinationType = combinationType;
    }

    public void setMainRank(int mainRank) {
        this.mainRank = mainRank;
    }

    public List<Card> getCards() {
        return cards;
    }
    public CombinationType getCombinationType() {
        return combinationType;
    }

    public int getMainRank() {
        return mainRank;
    }

    public int getSecondaryRank() {
        return secondaryRank;
    }

    public void setSecondaryRank(int secondaryRank) {
        this.secondaryRank = secondaryRank;
    }

    @Id
    @GeneratedValue
    private Long id;
    @OneToMany(mappedBy = "playableCombinations")
    List<Card> cards;
    @ManyToOne
    @JsonBackReference(value = "combination-player")
    private Player player;
    CombinationType combinationType;
    int mainRank;
    int secondaryRank;
}