package rocks.process.acrm.data.domain;

public class Card implements Comparable<Card> {
    private int rank;
    private Suit suit;
    private static final int MAX_RANK = 14;
    private static final int MIN_RANK = 2;

    public Card(int rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }


    @Override
    public int compareTo(Card i){
        if (this.rank == i.rank) return 0;
        else if (this.rank > i.rank) return 1;
        else return -1;

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
