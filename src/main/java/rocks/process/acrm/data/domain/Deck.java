package rocks.process.acrm.data.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {
    private ArrayList<Card> cards;

    public Deck(){
        cards = new ArrayList<Card>();
        addJade();
        addSword();
        addPagoda();
        addStar();
        addSpecial();
        this.shuffle();
    }

    private int getSize() {
        return this.cards.size();
    }

    private void addJade() {
        for (int i = Card.getMIN_RANK(); i < Card.getMAX_RANK(); i++) {
        cards.add(new Card(i, Suit.JADE));
    }
}

    private void addSword() {
        for (int i = Card.getMIN_RANK(); i < Card.getMAX_RANK(); i++) {
            cards.add(new Card(i, Suit.SWORD));
        }
    }

    private void addPagoda() {
        for (int i = Card.getMIN_RANK(); i < Card.getMAX_RANK(); i++) {
            cards.add(new Card(i, Suit.PAGODA));
        }
    }

    private void addStar() {
        for (int i = Card.getMIN_RANK(); i < Card.getMAX_RANK(); i++) {
            cards.add(new Card(i, Suit.STAR));
        }
    }

    private void addSpecial() {
        cards.add(new Card(25,Suit.DRAGON));
        cards.add(new Card(25,Suit.PHOENIX));
        cards.add(new Card(0,Suit.MAHJONG));
        cards.add(new Card(0,Suit.DOG));

    }

    private void shuffle(){
        Collections.shuffle(this.cards);

    }

    public Card giveRandomCard() {
        Random random = new Random();
        Card temp = cards.get(random.nextInt(this.getSize()));
        this.removeCardFromDeck(temp);
        return temp;
    }

    private void removeCardFromDeck(Card card){
        cards.remove(card);
    }
}



