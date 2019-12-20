package masterminds.tichu.server.data.domain;

import java.util.List;

/**
 * Author(S): Nelson Braillard
 */
public class MoveHandler {
    public Long getPlayerID() {
        return playerID;
    }

    public void setPlayerID(Long playerID) {
        this.playerID = playerID;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public CombinationType getCombinationType() {
        return combinationType;
    }

    public void setCombinationType(CombinationType combinationType) {
        this.combinationType = combinationType;
    }

    private Long playerID;
    private List<Card> cards;
    private CombinationType combinationType;
}