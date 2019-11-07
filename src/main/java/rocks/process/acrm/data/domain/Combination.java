package rocks.process.acrm.data.domain;

import java.util.List;

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

    List<Card> cards;
    CombinationType combinationType;
    int mainRank;
    int secondaryRank;
}