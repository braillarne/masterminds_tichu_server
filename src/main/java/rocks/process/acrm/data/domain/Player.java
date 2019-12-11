package rocks.process.acrm.data.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

/**
 * Author(S): Nelson Braillard
 */
@Entity
public class Player {
    @Id
    @GeneratedValue
    private Long id;
    private Long profileID;
    private int avatarID;
    private String name;
    private boolean isHost;
    @ManyToOne
    private Team team;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "playerAssociatedToHand")
    private List<Card> hand;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "playerAssociatedToWon")
    private List<Card> wonCards;
    @ManyToMany
    private List<Combination> playableCombinations;
    private boolean isPlaying;
    @ManyToOne
    @JsonBackReference(value = "player-game")
    private Game game;
    private int receivedCardCounter;

    public int getReceivedCardCounter() {
        return receivedCardCounter;
    }

    public void setReceivedCardCounter(int receivedCardCounter) {
        this.receivedCardCounter = receivedCardCounter;
    }

    public Long getProfileID() {
        return profileID;
    }

    public void setProfileID(Long profileID) {
        this.profileID = profileID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isHost() {
        return isHost;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public Game getGame() {
        return game;
    }

    public void setHost(boolean host) {
        isHost = host;
    }

    public List<Combination> getPlayableCombinations() {
        return playableCombinations;
    }

    public void setPlayableCombinations(List<Combination> playableCombinations) {
        this.playableCombinations = playableCombinations;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAvatarID() {
        return avatarID;
    }

    public void setAvatarID(int avatarID) {
        this.avatarID = avatarID;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public List<Card> getWonCards() {
        return wonCards;
    }

    public void setWonCards(List<Card> wonCards) {
        this.wonCards = wonCards;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public void givePlayToken() {
        this.isPlaying = true;
    }

    public void removePlayToken() {
        this.isPlaying = false;
    }

    public void addOneCardToHand(Card card) {
        this.hand.add(card);
    }

    public void addCardsToHand(List<Card> cards) {
        this.hand.addAll(cards);
    }

    public void removeOneCardFromHand(Card card) {
        this.hand.remove(card);
    }

    public void removeCardsFromHand(List<Card> cards) {
        this.hand.removeAll(cards);
    }

    public void pushCards(Player targetPlayer, List<Card> pushedCards) {
        this.removeCardsFromHand(pushedCards);
        targetPlayer.addCardsToHand(pushedCards);
    }

    public List<Card> getHand() {
        return hand;
    }

    public void evaluatePlayableCombination(List<Card> hand) {
        List<Combination> playableCombination;
        // Clear the playable combinations
        playableCombination = null;

        //TODO evaluation logique

        setPlayableCombinations(playableCombination);
    }
}
