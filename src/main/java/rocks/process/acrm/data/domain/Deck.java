package rocks.process.acrm.data.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Author(S): Nelson Braillard
 */
@Entity
public class Deck {
    @Id
    @GeneratedValue
    private Long deckId;
    @OneToOne
    @JsonBackReference(value = "deck-game")
    private Game game;

    public Long getDeckId() {
        return deckId;
    }
}



