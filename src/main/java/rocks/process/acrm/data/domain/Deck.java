package rocks.process.acrm.data.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Entity
public class Deck {
    @Id
    @GeneratedValue
    private Long deckId;
    /*@OneToMany(mappedBy = "deck")
    private List<Card> cards;*/
    @OneToOne
    @JsonBackReference(value = "deck-game")
    private Game game;

    public Long getDeckId() {
        return deckId;
    }
}



