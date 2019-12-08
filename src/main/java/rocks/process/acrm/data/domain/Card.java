package rocks.process.acrm.data.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.ManyToAny;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
public class Card {
    @Id
    @GeneratedValue
    private Long id;
    private int rank;
    private Suit suit;
    private static final int MAX_RANK = 14;
    private static final int MIN_RANK = 2;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "playerAssociatedToHand_id")
    private Player playerAssociatedToHand;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "playerAssociatedToWon_id")
    private Player playerAssociatedToWon;
    @ManyToOne
    @JoinColumn(name = "combination_id")
    private Combination combination;
    @OneToOne
    @JsonBackReference(value = "card-game")
    private Game game;

    public void setGame(Game game) {
        this.game = game;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static int getMaxRank() {
        return MAX_RANK;
    }

    public static int getMinRank() {
        return MIN_RANK;
    }

    public void setPlayerAssociatedToHand(Player player) {
        this.playerAssociatedToHand = player;
    }

    public void setPlayerAssociatedToWon(Player player) {
        this.playerAssociatedToWon = player;
    }

    public Combination getCombination() {
        return combination;
    }

    public void setCombination(Combination Combination) {
        this.combination = Combination;
    }

    @Override
    public String toString() {
        return this.suit.toString() +" "+ this.rank;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public static int getMAX_RANK() {
        return MAX_RANK;
    }

    public static int getMIN_RANK() {
        return MIN_RANK;
    }
}
