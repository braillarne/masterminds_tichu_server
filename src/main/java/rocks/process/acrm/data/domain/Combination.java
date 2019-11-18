package rocks.process.acrm.data.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

@Entity
public class Combination {
    public Combination(List<Card> cards, CombinationType combinationType, int mainRank) {
        this.cards = cards;
        this.combinationType = combinationType;
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