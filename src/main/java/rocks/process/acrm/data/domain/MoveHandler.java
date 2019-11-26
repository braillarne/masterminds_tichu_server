package rocks.process.acrm.data.domain;

import java.util.List;

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

    private Long playerID;
    private List<Card> cards;
}