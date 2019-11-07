package rocks.process.acrm.data.domain;

import java.util.List;

public class Player {
    private String name;
    private boolean isHost;
    private List<Card> hand;

    public List<Combination> getPlayableCombinations() {
        return playableCombinations;
    }

    public void setPlayableCombinations(List<Combination> playableCombinations) {
        this.playableCombinations = playableCombinations;
    }

    private List<Combination> playableCombinations;
    private boolean isPlaying;

    public Player(String name, boolean isHost) {
        this.name = name;
        this.isHost = isHost;
    }

    public void givePlayToken(){
        this.isPlaying = true;
    }

    public void removePlayToken(){
        this.isPlaying = false;
    }

    public void addOneCardToHand(Card card){
        this.hand.add(card);
    }

    public void addCardsToHand(List<Card> cards){
        this.hand.addAll(cards);
    }

    public void removeOneCardFromHand(Card card){
        this.hand.remove(card);
    }

    public void removeCardsFromHand(List<Card> cards){
        this.hand.removeAll(cards);
    }

    public Card draw(Deck deck){
        Card tempCard = deck.giveRandomCard();
        this.addOneCardToHand(tempCard);
        return tempCard;
    }

    public void pushCards(Player targetPlayer, List<Card> pushedCards){
        this.removeCardsFromHand(pushedCards);
        targetPlayer.addCardsToHand(pushedCards);
    }

    public List<Card> getHand() {
        return hand;
    }

    public void evaluatePlayableCombination(List<Card> hand){
        List<Combination> playableCombination;
        // Clear the playable combinations
        playableCombination=null;

        //TODO evaluation logique

        setPlayableCombinations(playableCombination);
    }
}
