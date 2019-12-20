package masterminds.tichu.server.data.domain;

import javax.persistence.*;
import java.util.List;

/**
 * Author(S): Nelson Braillard
 */
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

    public List<Card> getCards() {
        return cards;
    }


    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private Player player;
    CombinationType combinationType;
    int mainRank;
    int secondaryRank;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "combination")
    private List<Card> cards;
}